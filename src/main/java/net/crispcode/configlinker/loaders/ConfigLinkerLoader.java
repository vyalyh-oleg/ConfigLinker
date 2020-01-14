package net.crispcode.configlinker.loaders;

import net.crispcode.configlinker.ConfigDescription;
import net.crispcode.configlinker.exceptions.PropertyLoadException;

import java.util.HashMap;
import java.util.Properties;


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
