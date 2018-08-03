package com.configlinker;

import com.configlinker.exceptions.AnnotationAnalyzeException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.loaders.LoaderService;
import com.configlinker.proxy.ConfigProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public final class ConfigSetFactory {

	private ConfigSetFactory() {
	}
	
	public static ConfigSet create(Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		return create(FactoryConfigBuilder.create(), configInterfaces);
	}
	
	public static ConfigSet create(FactoryConfigBuilder builder, Class<?>... configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		HashSet<Class<?>> setConfigInterfaces = new HashSet<>(Arrays.asList(configInterfaces));
		// TODO: check on duplicate interfaces and throw an error if any found
		return create(builder, setConfigInterfaces);
	}
	
	public static ConfigSet create(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException {
		return create(FactoryConfigBuilder.create(), configInterfaces);
	}
	
	public static ConfigSet create(FactoryConfigBuilder builder, Set<Class<?>> configInterfaces) throws PropertyMapException, AnnotationAnalyzeException
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
