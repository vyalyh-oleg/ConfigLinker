package com.configlinker.loaders;

import com.configlinker.ConfigDescription;
import com.configlinker.exceptions.PropertyLoadException;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


final class ConfigLinkerLoader extends AbstractLoader
{
	//private ExecutorService scheduledExecutorService = Executors.newCachedThreadPool(new ConfigLinkerThreadFactory(this.getClass().getSimpleName()));
	
	ConfigLinkerLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException
	{
		super(configDescriptions);
	}
	
	@Override
	protected void prepareLoader()
	{
	
	}
	
	@Override
	protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException
	{
		return null;
	}
	
	@Override
	protected void startTrackChanges() throws PropertyLoadException
	{
		super.startTrackChanges();
	}
	
	@Override
	protected void stopTrackChanges() throws PropertyLoadException
	{
		super.stopTrackChanges();
	}
}
