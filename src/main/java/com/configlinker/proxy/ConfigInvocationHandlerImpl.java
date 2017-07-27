package com.configlinker.proxy;

import com.configlinker.ConfigDescription;
import com.configlinker.loaders.LoaderService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;


class ConfigInvocationHandlerImpl implements InvocationHandler {

	private final HashMap<Class<?>, ConfigDescription> mapConfigDescriptions;
	private final LoaderService loaderService;

	public ConfigInvocationHandlerImpl(HashMap<Class<?>, ConfigDescription> mapConfigDescriptions, LoaderService loaderService) {
		this.mapConfigDescriptions = mapConfigDescriptions;
		this.loaderService = loaderService;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ConfigDescription configDescription = mapConfigDescriptions.get(method.getDeclaringClass());
		ConfigDescription.PropertyDescription propertyDescription = null;
		if (configDescription != null)
			propertyDescription = configDescription.getBoundPropertyMethods().get(method);

		if (propertyDescription != null)
			return loaderService.getProperty(configDescription, propertyDescription, method, args);

		return method.invoke(proxy, args);
	}
}
