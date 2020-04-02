/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.loaders;

import net.crispcode.configlinker.ConfigDescription;
import net.crispcode.configlinker.Loggers;
import net.crispcode.configlinker.enums.TrackPolicy;
import net.crispcode.configlinker.exceptions.PropertyLoadException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class PropertyFileLoader extends AbstractLoader
{
	private ExecutorService executorService;
	private Map<Path, Set<ConfigDescription>> watchedFiles = new HashMap<>();
	private volatile boolean trackChanges = false;
	private HashSet<WatchKey> watchKeys;
	
	PropertyFileLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyValidateException, PropertyLoadException, PropertyMatchException
	{
		super(configDescriptions);
		
		// invoke only in final loader instance (subclass of 'AbstractLoader')
		this.prepareLoader();
		this.loadProperties();
		this.startTrackChanges();
	}
	
	protected Path getFullFilePath(ConfigDescription configDescription)
	{
		Path fullFilePath = Paths.get(configDescription.getSourcePath()).normalize().toAbsolutePath();
		
		if (!Files.exists(fullFilePath))
		{
			throw new PropertyLoadException(
				"Configuration file '" + fullFilePath.getFileName().toString() + "' not exists, full file path: '" + fullFilePath
					.toString() + "'; see annotation parameter @BoundObject.sourcePath() on interface '" + configDescription.getConfInterface()
					.getName() + "'.").logAndReturn();
		}
		
		return fullFilePath;
	}
	
	@Override
	protected void prepareLoader() throws PropertyLoadException
	{
		Path fullFilePath;
		for (ConfigDescription description : getConfigDescriptions())
		{
			fullFilePath = getFullFilePath(description);
			if (description.getTrackPolicy() == TrackPolicy.ENABLE)
				watchedFiles.computeIfAbsent(fullFilePath, path -> new HashSet<>()).add(description);
		}
	}
	
	@Override
	protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException
	{
		Path fullFilePath = getFullFilePath(configDescription);
		return readPropertiesFileFromDisk(fullFilePath, configDescription);
	}
	
	private Properties readPropertiesFileFromDisk(Path fullFilePath, ConfigDescription configDescription)
	{
		try (BufferedReader propFileReader = Files.newBufferedReader(fullFilePath, configDescription.getCharset()))
		{
			Properties newProperties = new Properties();
			newProperties.load(propFileReader);
			propFileReader.close();
			return newProperties;
		}
		catch (IOException e)
		{
			throw new PropertyLoadException(
				"Error during loading raw properties from file '" + fullFilePath + "' with charset '" + configDescription.getCharset().toString()
					+ "', config interface: '" + configDescription.getConfInterface().getName() + "'.", e)
				.logAndReturn();
		}
	}
	
	@Override
	protected void startTrackChanges() throws PropertyLoadException
	{
		if (watchedFiles.isEmpty())
			return;
		
		trackChanges = true;
		executorService = Executors.newCachedThreadPool(new ConfigLinkerThreadFactory(this.getClass().getSimpleName()));
		watchKeys = new HashSet<>();
		
		try
		{
			HashMap<FileSystem, HashSet<Path>> fileSystems = watchedFiles.keySet().stream()
				.collect(
					HashMap::new,
					(map, path) -> map.computeIfAbsent(path.getFileSystem(), fileSystem -> new HashSet<>()).add(path.getParent()),
					HashMap::putAll);
			
			for (Map.Entry<FileSystem, HashSet<Path>> fileSystemEntry : fileSystems.entrySet())
			{
				HashMap<WatchKey, Path> registeredDirs = new HashMap<>();
				WatchService watchService = fileSystemEntry.getKey().newWatchService();
				for (Path dir : fileSystemEntry.getValue())
				{
					WatchKey watchKey = dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
						StandardWatchEventKinds.ENTRY_MODIFY);
					registeredDirs.put(watchKey, dir);
				}
				
				executorService.submit(() -> watchLoop(Collections.unmodifiableMap(registeredDirs), watchService));
				watchKeys.addAll(registeredDirs.keySet());
			}
		}
		catch (IOException e)
		{
			throw new PropertyLoadException("Error during creating file watcher for configuration file.", e).logAndReturn();
		}
	}
	
	private void watchLoop(final Map<WatchKey, Path> registeredDirs, final WatchService watchService)
	{
		while (trackChanges)
		{
			WatchKey watchKey;
			try
			{
				watchKey = watchService.take();
			}
			catch (InterruptedException x)
			{
				break;
			}
			
			if (!watchKey.isValid())
			{
				registeredDirs.remove(watchKey);
				if (registeredDirs.isEmpty())
					break;
				continue;
			}
			
			for (WatchEvent<?> event : watchKey.pollEvents())
			{
				WatchEvent.Kind<?> kind = event.kind();
				Path relativeFilePath = ((Path) event.context()).normalize();
				Path fullFilePath = registeredDirs.get(watchKey).resolve(relativeFilePath);
				
				Set<ConfigDescription> configDescriptions = watchedFiles.get(fullFilePath);
				if (configDescriptions == null)
					continue;
				
				if (kind == StandardWatchEventKinds.OVERFLOW)
				{
					Loggers.getMainLogger().info("Lost some events for configuration file: '{}'.", fullFilePath);
					continue;
				}
				
				if (kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE)
				{
					Loggers.getMainLogger().info("Configuration file has been changed: '{}'.", fullFilePath);
					this.refreshProperties(configDescriptions);
					continue;
				}
				
				if (kind == StandardWatchEventKinds.ENTRY_DELETE)
				{
					Loggers.getMainLogger().info("Configuration file has been deleted: '{}'. The changes won't be applied.", fullFilePath);
				}
			}
			
			if (!watchKey.reset())
			{
				registeredDirs.remove(watchKey);
				Loggers.getMainLogger().info("Watch key cancelled. Configuration dir: '{}'.", watchKey.watchable().toString());
				watchKey.cancel();
				
				if (registeredDirs.isEmpty())
					break;
			}
		}
	}
	
	@Override
	protected void stopTrackChanges()
	{
		if (!trackChanges)
			return;
		
		trackChanges = false;
		watchKeys.forEach(WatchKey::cancel);
		executorService.shutdown();
		executorService = null;
	}
}
