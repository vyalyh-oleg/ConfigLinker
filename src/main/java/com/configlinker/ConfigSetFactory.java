package com.configlinker;

import com.configlinker.exceptions.AnnotationAnalyzeException;
import com.configlinker.exceptions.ConfigSetException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.loaders.LoaderService;
import com.configlinker.proxy.ConfigProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Is initial point in your code when you want to work with the library. It contains static methods which create and return {@link ConfigSet}.
 */
public final class ConfigSetFactory
{
	
	private ConfigSetFactory()
	{
	}
	
	/**
	 * @param configInterfaces Interfaces annotated with {@link com.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link com.configlinker.annotations.BoundProperty}. A set of interfaces should be a Set (so the duplicates are just ignored).
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException
	 * @throws PropertyMapException
	 */
	public static ConfigSet create(Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
	/**
	 * @param builder {@link FactorySettingsBuilder}
	 * @param configInterfaces Interfaces annotated with {@link com.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link com.configlinker.annotations.BoundProperty}. A set of interfaces should be a Set (so the duplicates are just ignored).
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException
	 * @throws PropertyMapException
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
	 * @param configInterfaces Interfaces annotated with {@link com.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link com.configlinker.annotations.BoundProperty}.
	 * @return {@link ConfigSet}
	 * @throws AnnotationAnalyzeException
	 * @throws PropertyMapException
	 */
	public static ConfigSet create(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
	/**
	 * @param builder {@link FactorySettingsBuilder}
	 * @param configInterfaces Interfaces annotated with {@link com.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link com.configlinker.annotations.BoundProperty}.
	 * @return {@link ConfigSet}
	 * @throws PropertyMapException
	 * @throws AnnotationAnalyzeException
	 */
	public static ConfigSet create(FactorySettingsBuilder builder, Set<Class<?>> configInterfaces) throws PropertyMapException, AnnotationAnalyzeException
	{
		builder.close();
		configInterfaces = Collections.unmodifiableSet(new HashSet<>(configInterfaces));
		AnnotationScanner annotationScanner = new AnnotationScanner(builder);
		HashMap<Class<?>, ConfigDescription> mapConfigDescriptions = annotationScanner.scan(configInterfaces);
		LoaderService loaderService = LoaderService.create(mapConfigDescriptions);
		Proxy configProxy = ConfigProxyFactory.create(mapConfigDescriptions, loaderService);
		
		Runnable stopTrackChanges = loaderService::stopTrackChanges;
		return new ConfigSet(configProxy, stopTrackChanges);
	}
	
}
