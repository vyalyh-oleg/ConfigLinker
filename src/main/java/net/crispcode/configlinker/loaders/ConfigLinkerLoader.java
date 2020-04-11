/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <crispcode.net@gmail.com>

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
