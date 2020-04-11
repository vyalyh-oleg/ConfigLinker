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
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.exceptions.PropertyLoadException;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


final public class LoaderService
{
	private final Map<SourceScheme, AbstractLoader> loaders;
	
	private LoaderService(EnumMap<SourceScheme, AbstractLoader> loaders)
	{
		this.loaders = loaders;
	}
	
	public void stopTrackChanges()
	{
		this.loaders.values().forEach(AbstractLoader::stopTrackChanges);
	}
	
	public static LoaderService create(HashMap<Class<?>, ConfigDescription> configDescriptions)
	{
		EnumMap<SourceScheme, HashMap<Class<?>, ConfigDescription>> schemeDescriptions = configDescriptions.values().stream()
			.collect(
				() -> new EnumMap<SourceScheme, HashMap<Class<?>, ConfigDescription>>(SourceScheme.class),
				(map, confDescr) -> map.computeIfAbsent(confDescr.getSourceScheme(), srcScheme -> new HashMap<>())
					.put(confDescr.getConfInterface(), confDescr),
				EnumMap::putAll);
		
		EnumMap<SourceScheme, AbstractLoader> loaders = new EnumMap<>(SourceScheme.class);
		
		HashMap<Class<?>, ConfigDescription> descriptions = schemeDescriptions.get(SourceScheme.CLASSPATH);
		if (descriptions != null)
			loaders.put(SourceScheme.CLASSPATH, new ClasspathLoader(descriptions));
		
		descriptions = schemeDescriptions.get(SourceScheme.FILE);
		if (descriptions != null)
			loaders.put(SourceScheme.FILE, new PropertyFileLoader(descriptions));
		
		descriptions = schemeDescriptions.get(SourceScheme.HTTP);
		if (descriptions != null)
			loaders.put(SourceScheme.HTTP, new HttpLoader(descriptions));
		
		descriptions = schemeDescriptions.get(SourceScheme.CONFIG_LINKER_SERVER);
		if (descriptions != null)
			loaders.put(SourceScheme.CONFIG_LINKER_SERVER, new ConfigLinkerLoader(descriptions));
		
		return new LoaderService(loaders);
	}
	
	public Object getProperty(ConfigDescription configDescription, ConfigDescription.PropertyDescription propertyDescription, Method method, Object[] proxyArgs)
	{
		HashMap<String, String> methodArguments = null;
		if (propertyDescription.getDynamicVariableNames() != null)
			if (propertyDescription.getDynamicVariableNames().length != proxyArgs.length)
				throw new PropertyLoadException(
					"No obligatory arguments for method '" + configDescription.getConfInterface().getName() + "#" + method.getName() + "'.");
			else
			{
				methodArguments = new HashMap<>();
				for (int i = 0; i < propertyDescription.getDynamicVariableNames().length; i++)
				{
					methodArguments.put(propertyDescription.getDynamicVariableNames()[i], proxyArgs[i].toString());
				}
			}
		
		return loaders.get(configDescription.getSourceScheme()).getProperty(configDescription, propertyDescription, method, methodArguments);
	}
}