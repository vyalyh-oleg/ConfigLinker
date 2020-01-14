package net.crispcode.configlinker.proxy;

import net.crispcode.configlinker.ConfigDescription;
import net.crispcode.configlinker.loaders.LoaderService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Set;


public class ConfigProxyFactory
{
	
	public static Proxy create(HashMap<Class<?>, ConfigDescription> mapConfigDescriptions, LoaderService loaderService)
	{
		ConfigInvocationHandlerImpl configInvocationHandler = new ConfigInvocationHandlerImpl(mapConfigDescriptions, loaderService);
		Set<Class<?>> configInterfaces = mapConfigDescriptions.keySet();
		return (Proxy) Proxy.newProxyInstance(ConfigProxyFactory.class.getClassLoader(),
			configInterfaces.toArray(new Class<?>[configInterfaces.size()]),
			configInvocationHandler);
	}
}
