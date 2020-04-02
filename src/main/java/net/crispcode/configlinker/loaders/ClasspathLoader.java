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
import net.crispcode.configlinker.exceptions.PropertyLoadException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;

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
				.getSimpleName() + "' couldn't get resource from classpath and convert it to ordinary 'Path' object. Actual resource URL:'" + resource
				.toString() + "'.")
				.logAndReturn();
		}
		
		return fullFilePath;
	}
}
