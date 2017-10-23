package com.configlinker;

import com.configlinker.exceptions.AnnotationAnalyzeException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.proxy.ConfigProxyFactory;
import com.configlinker.loaders.LoaderService;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public final class ConfigSetFactory {

	private ConfigSetFactory() {
	}

	public static ConfigSet create(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException {
		return create(configInterfaces, new FactoryConfigBuilder());
	}

	public static ConfigSet create(Set<Class<?>> configInterfaces, FactoryConfigBuilder builder) throws PropertyMapException, AnnotationAnalyzeException {
		builder.close();
		configInterfaces = Collections.unmodifiableSet(new HashSet<>(configInterfaces));
		AnnotationScanner annotationScanner = new AnnotationScanner(builder);
		HashMap<Class<?>, ConfigDescription> mapConfigDescriptions = annotationScanner.scan(configInterfaces);
		LoaderService loaderService = LoaderService.create(mapConfigDescriptions);
		Proxy configProxy = (Proxy) ConfigProxyFactory.create(mapConfigDescriptions, loaderService);

		return new ConfigSet(configProxy);
	}

}
