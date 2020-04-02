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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


final class HttpLoader extends AbstractLoader
{
	private ScheduledExecutorService executorService;
	private Map<URL, Set<ConfigDescription>> watchedFiles = new HashMap<>();
	private boolean trackChanges = false;
	
	HttpLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException
	{
		super(configDescriptions);
		
		// invoke only in final loader instance (subclass of 'AbstractLoader')
		this.prepareLoader();
		this.loadProperties();
		this.startTrackChanges();
	}
	
	@Override
	protected void prepareLoader() throws PropertyLoadException
	{
		URL url;
		for (ConfigDescription description : getConfigDescriptions())
		{
			try
			{
				url = new URL(description.getSourcePath());
			}
			catch (MalformedURLException e)
			{
				throw new PropertyLoadException(
					"Wrong HTTP/S URL '" + description.getSourcePath() + "' in annotation parameter @BoundObject.sourcePath() on interface '" + description
						.getConfInterface().getName() + "'.").logAndReturn();
			}
			if (description.getTrackPolicy() == TrackPolicy.ENABLE)
				watchedFiles.computeIfAbsent(url, url1 -> new HashSet<>()).add(description);
		}
	}
	
	@Override
	protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException
	{
		URL url;
		try
		{
			url = new URL(configDescription.getSourcePath());
		}
		catch (MalformedURLException e)
		{
			throw new PropertyLoadException("Wrong HTTP/S URL '" + configDescription
				.getSourcePath() + "' in annotation parameter @BoundObject.sourcePath() on interface '" + configDescription.getConfInterface().getName() + "'.",
				e).logAndReturn();
		}
		
		BufferedReader configReader = null;
		try
		{
			URLConnection connection = url.openConnection();
			
			if (configDescription.getHttpHeaders() != null)
				configDescription.getHttpHeaders().forEach(connection::setRequestProperty);
			
			connection.setUseCaches(true);
			connection.connect();
			configReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), configDescription.getCharset()));
			Properties properties = new Properties();
			properties.load(configReader);
			return properties;
		}
		catch (IOException e)
		{
			throw new PropertyLoadException(
				"Error during loading raw properties from URL '" + url.toString() + "' with charset '" + configDescription.getCharset()
					.toString() + "', config interface: '" + configDescription.getConfInterface().getName() + "'.", e).logAndReturn();
		}
		finally
		{
			if (configReader != null)
				try
				{
					configReader.close();
				}
				catch (IOException e)
				{
					Loggers.getMainLogger().warn("Can not close URLConnection for URL: '{}', config interface: '{}'.", url.toString(),
						configDescription.getConfInterface().getName(), e);
				}
		}
	}
	
	@Override
	protected void startTrackChanges() throws PropertyLoadException
	{
		if (watchedFiles.isEmpty())
			return;
		
		executorService = Executors.newScheduledThreadPool(2, new ConfigLinkerThreadFactory(this.getClass().getSimpleName()));
		trackChanges = true;
		
		for (Map.Entry<URL, Set<ConfigDescription>> configDescriptionsEntry : watchedFiles.entrySet())
		{
			int intervalInSeconds = configDescriptionsEntry.getValue().stream().mapToInt(ConfigDescription::getTrackingInterval).min().orElse(15);
			executorService.scheduleWithFixedDelay(
				() -> this.refreshProperties(configDescriptionsEntry.getValue()),
				(long) intervalInSeconds, (long) intervalInSeconds, TimeUnit.SECONDS);
		}
	}
	
	@Override
	protected void stopTrackChanges() throws PropertyLoadException
	{
		if (!trackChanges)
			return;
		
		trackChanges = false;
		executorService.shutdown();
		executorService = null;
	}
}
