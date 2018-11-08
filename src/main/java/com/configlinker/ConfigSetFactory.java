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


public final class ConfigSetFactory {

	private ConfigSetFactory() {
	}
	
	public static ConfigSet create(Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
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
	
	public static ConfigSet create(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException {
		return create(FactorySettingsBuilder.create(), configInterfaces);
	}
	
	public static ConfigSet create(FactorySettingsBuilder builder, Set<Class<?>> configInterfaces) throws PropertyMapException, AnnotationAnalyzeException
	{
		builder.close();
		configInterfaces = Collections.unmodifiableSet(new HashSet<>(configInterfaces));
		AnnotationScanner annotationScanner = new AnnotationScanner(builder);
		HashMap<Class<?>, ConfigDescription> mapConfigDescriptions = annotationScanner.scan(configInterfaces);
		LoaderService loaderService = LoaderService.create(mapConfigDescriptions);
		Proxy configProxy = (Proxy) ConfigProxyFactory.create(mapConfigDescriptions, loaderService);

		return new ConfigSet(configProxy);
	}
	
}
