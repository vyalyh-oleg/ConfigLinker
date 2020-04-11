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


package net.crispcode.configlinker;

import net.crispcode.configlinker.exceptions.AnnotationAnalyzeException;
import net.crispcode.configlinker.exceptions.ConfigSetException;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.loaders.LoaderService;
import net.crispcode.configlinker.proxy.ConfigProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Is initial point in your code when you want to work with the library. It contains static methods which create and return {@link ConfigSet}.
 */
public final class ConfigSetFactory
{
	
	private ConfigSetFactory()
	{
	}
	
	/**
	 * @param configInterfaces Interfaces annotated with {@link net.crispcode.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link net.crispcode.configlinker.annotations.BoundProperty}. A set of interfaces should be a Set (so the duplicates are just ignored).
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException -
	 * @throws PropertyMapException -
	 */
	public static ConfigSet create(Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
	/**
	 * @param builder {@link FactorySettingsBuilder}
	 * @param configInterfaces Interfaces annotated with {@link net.crispcode.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link net.crispcode.configlinker.annotations.BoundProperty}. A set of interfaces should be a Set (so the duplicates are just ignored).
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException -
	 * @throws PropertyMapException -
	 */
	public static ConfigSet create(FactorySettingsBuilder builder, Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		HashSet<Class<?>> setConfigInterfaces = new HashSet<>();
		for (Class<?> clazz : configInterfaces)
		{
			if (!setConfigInterfaces.add(clazz))
				throw new ConfigSetException("Duplicate interface found: '" + clazz.getName() + "'.");
		}
		return create(builder, setConfigInterfaces);
	}
	
	/**
	 * @param configInterfaces Interfaces annotated with {@link net.crispcode.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link net.crispcode.configlinker.annotations.BoundProperty}.
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException -
	 * @throws PropertyMapException -
	 */
	public static ConfigSet create(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
	/**
	 * @param builder {@link FactorySettingsBuilder}
	 * @param configInterfaces Interfaces annotated with {@link net.crispcode.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link net.crispcode.configlinker.annotations.BoundProperty}.
	 * @return {@link ConfigSet}
	 * @throws PropertyMapException -
	 * @throws AnnotationAnalyzeException -
	 */
	public static ConfigSet create(FactorySettingsBuilder builder, Set<Class<?>> configInterfaces) throws PropertyMapException, AnnotationAnalyzeException
	{
		builder.closeBuilder();
		configInterfaces = Collections.unmodifiableSet(new HashSet<>(configInterfaces));
		AnnotationScanner annotationScanner = new AnnotationScanner(builder);
		HashMap<Class<?>, ConfigDescription> mapConfigDescriptions = annotationScanner.scan(configInterfaces);
		LoaderService loaderService = LoaderService.create(mapConfigDescriptions);
		Proxy configProxy = ConfigProxyFactory.create(mapConfigDescriptions, loaderService);
		
		Runnable stopTrackChanges = loaderService::stopTrackChanges;
		return new ConfigSet(configProxy, stopTrackChanges);
	}
	
}
