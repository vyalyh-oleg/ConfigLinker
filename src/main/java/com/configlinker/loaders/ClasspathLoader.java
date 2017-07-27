package com.configlinker.loaders;

import com.configlinker.ConfigDescription;
import com.configlinker.exceptions.PropertyLoadException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;


final class ClasspathLoader extends ALoader {
	ClasspathLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException, PropertyValidateException, PropertyMatchException {
		super(configDescriptions);
		this.prepareLoader();
		this.loadProperties();
	}

	@Override
	protected void prepareLoader() {

	}

	@Override
	protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException {
		Class<?> configInterface = configDescription.getConfInterface();
		Path relativeFilePath = Paths.get(configDescription.getSourcePath()).normalize();
		if (relativeFilePath.isAbsolute()) {
			// TODO: log
			throw new PropertyLoadException("'" + configDescription.getConfInterface().getName() + "' doesn't accept absolute path, only relative, because ConfigLinker searches configuration files in your classpath directories or 'jar' files. Actual source path:'" + relativeFilePath + "'.");
		}

		URL resource = ClasspathLoader.class.getClassLoader().getResource(relativeFilePath.toString());
		if (resource == null) {
			// TODO: log
			throw new PropertyLoadException("Configuration file '" + relativeFilePath + "' not exists; see annotation parameter @BoundObject.sourcePath() on interface '" + configDescription.getConfInterface().getName() + "'.");
		}

		Path fullFilePath;
		try {
			fullFilePath = Paths.get(resource.toURI());
		} catch (URISyntaxException e) {
			// TODO: log
			throw new PropertyLoadException("'" + this.getClass().getSimpleName() + "' couldn't get resource from classpath and convert it to ordinary 'Path' object. Actual resource URL:'" + resource.toString() + "'.");
		}

		try {
			BufferedReader propFileReader = Files.newBufferedReader(fullFilePath, configDescription.getCharset());
			Properties newProperties = new Properties();
			newProperties.load(propFileReader);
			propFileReader.close();
			return newProperties;
		} catch (IOException e) {
			// TODO: log
			throw new PropertyLoadException("Error during loading raw properties from file '" + fullFilePath + "', config interface: '" + configInterface.getName() + "'.", e);
		}
	}

	@Override
	protected void startTrackChanges() throws PropertyLoadException {
		throw new PropertyLoadException("'" + this.getClass().getSimpleName() + "' doesn't support tracking changes.");
	}

	@Override
	protected void stopTrackChanges() throws PropertyLoadException {
		throw new PropertyLoadException("'" + this.getClass().getSimpleName() + "' doesn't support tracking changes.");
	}
}
