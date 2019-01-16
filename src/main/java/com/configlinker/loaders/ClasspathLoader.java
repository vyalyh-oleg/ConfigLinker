package com.configlinker.loaders;

import com.configlinker.ConfigDescription;
import com.configlinker.exceptions.PropertyLoadException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


final class ClasspathLoader extends PropertyFileLoader
{
	ClasspathLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException, PropertyValidateException, PropertyMatchException
	{
		super(configDescriptions);
	}
	
	@Override
	final protected Path getFullFilePath(ConfigDescription configDescription)
	{
		Path relativeFilePath = Paths.get(configDescription.getSourcePath()).normalize();
		if (relativeFilePath.isAbsolute())
		{
			throw new PropertyLoadException("'" + configDescription.getConfInterface().getName()
				+ "' doesn't accept absolute path, only relative, because ConfigLinker searches configuration files in your classpath directories or 'jar' files. Actual source path:'"
				+ relativeFilePath + "'.")
				.logAndReturn();
		}
		
		URL resource = ClasspathLoader.class.getClassLoader().getResource(relativeFilePath.toString());
		if (resource == null)
			throw new PropertyLoadException(
				"Configuration file '" + relativeFilePath + "' not exists; see annotation parameter @BoundObject.sourcePath() on interface '"
					+ configDescription.getConfInterface().getName() + "'.")
				.logAndReturn();
		
		Path fullFilePath;
		try
		{
			fullFilePath = Paths.get(resource.toURI());
		}
		catch (URISyntaxException e)
		{
			throw new PropertyLoadException("'" + this.getClass()
				.getSimpleName() + "' couldn't get resource from classpath and convert it to ordinary 'Path' object. Actual resource URL:'" + resource.toString() + "'.")
				.logAndReturn();
		}
		
		return fullFilePath;
	}
}
