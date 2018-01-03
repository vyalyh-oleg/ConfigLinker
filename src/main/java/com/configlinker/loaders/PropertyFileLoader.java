package com.configlinker.loaders;

import com.configlinker.ConfigDescription;
import com.configlinker.Loggers;
import com.configlinker.annotations.BoundObject;
import com.configlinker.exceptions.PropertyLoadException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


final class PropertyFileLoader extends AbstractLoader {
	private ExecutorService executorService;
	private Map<Path, Set<ConfigDescription>> watchedFiles = new HashMap<>();
	private boolean trackChanges = false;

	PropertyFileLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyValidateException, PropertyLoadException, PropertyMatchException {
		super(configDescriptions);

		// invoke only in final loader instance (subclass of 'AbstractLoader')
		this.prepareLoader();
		this.loadProperties();
		this.startTrackChanges();
	}

	@Override
	protected void prepareLoader() throws PropertyLoadException {
		Path fullFilePath;
		for (ConfigDescription description : getConfigDescriptions()) {
			try {
				fullFilePath = Paths.get(description.getSourcePath()).normalize().toAbsolutePath();
			} catch (InvalidPathException e) {
				throw new PropertyLoadException("Wrong file path '" + description.getSourcePath() + "' in annotation parameter @BoundObject.sourcePath() on interface '" + description.getConfInterface().getName() + "'.").logAndReturn().logAndReturn();
			}

			if (description.getTrackPolicy() == BoundObject.TrackPolicy.ENABLE)
				watchedFiles.computeIfAbsent(fullFilePath, path -> new HashSet<>()).add(description);
		}
	}

	@Override
	protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException {
		Path fullFilePath = Paths.get(configDescription.getSourcePath()).normalize().toAbsolutePath();
		try {
			if (!Files.exists(fullFilePath))
				throw new PropertyLoadException("Configuration file '" + fullFilePath.getFileName().toString() + "' not exists, full file path: '" + fullFilePath.toString() + "'; see annotation parameter @BoundObject.sourcePath() on interface '" + configDescription.getConfInterface().getName() + "'.").logAndReturn();

			BufferedReader propFileReader = Files.newBufferedReader(fullFilePath, configDescription.getCharset());
			Properties newProperties = new Properties();
			newProperties.load(propFileReader);
			propFileReader.close();
			return newProperties;
		} catch (IOException e) {
			throw new PropertyLoadException("Error during loading raw properties from file '" + fullFilePath + "' with charset '" + configDescription.getCharset().toString() + "', config interface: '" + configDescription.getConfInterface().getName() + "'.", e).logAndReturn();
		}
	}

	@Override
	protected void startTrackChanges() throws PropertyLoadException {
		if (watchedFiles.isEmpty())
			return;

		executorService = Executors.newCachedThreadPool(new ConfigLinkerThreadFactory(this.getClass().getSimpleName()));
		trackChanges = true;

		try {
			HashMap<FileSystem, HashSet<Path>> fileSystems = watchedFiles.keySet().stream()
			.collect(
				HashMap::new,
				(map, path) -> map.computeIfAbsent(path.getFileSystem(), fileSystem -> new HashSet<>()).add(path.getParent()),
				HashMap::putAll);

			for (Map.Entry<FileSystem, HashSet<Path>> fileSystemEntry : fileSystems.entrySet()) {
				WatchService watchService = fileSystemEntry.getKey().newWatchService();
				for (Path dir : fileSystemEntry.getValue())
					dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

				executorService.submit(() -> watchLoop(watchService));
			}
		} catch (IOException e) {
			throw new PropertyLoadException("Error during creating file watcher for configuration file.", e).logAndReturn();
		}
	}

	private void watchLoop(WatchService watchService) {
		while (trackChanges) {
			WatchKey key;
			try {
				key = watchService.take();
			} catch (InterruptedException x) {
				return;
			}

			if (!key.isValid())
				continue;

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				Path fullFilePath = ((Path) event.context()).normalize().toAbsolutePath();

				Set<ConfigDescription> configDescriptions = watchedFiles.get(fullFilePath);
				if (configDescriptions == null)
					continue;

				if (kind == StandardWatchEventKinds.OVERFLOW) {
					Loggers.getMainLogger().info("Lost some events for configuration file: '{}'.", fullFilePath);
					continue;
				}

				if (kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE) {
					Loggers.getMainLogger().info("Configuration file has changed: '{}'.", fullFilePath);
					this.refreshProperties(configDescriptions);
					continue;
				}

				if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
					Loggers.getMainLogger().info("Configuration file has been deleted: '{}'. The changes won't be applied.", fullFilePath);
				}
			}

			if (!key.reset()) {
				Loggers.getMainLogger().info("Watch key cancelled. Configuration file: '{}'.", key.watchable().toString());
				key.cancel();
			}
		}
	}

	@Override
	protected void stopTrackChanges() {
		trackChanges = false;
		executorService.shutdown();
		executorService = null;
	}
}
