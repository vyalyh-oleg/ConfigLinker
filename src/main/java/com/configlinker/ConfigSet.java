package com.configlinker;

import com.configlinker.exceptions.ConfigSetException;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class ConfigSet
{
	private final Proxy configProxy;
	private final List<Class<?>> configInterfaces;
	
	ConfigSet(Proxy configProxy)
	{
		this.configProxy = configProxy;
		this.configInterfaces = Collections.unmodifiableList(Arrays.asList(this.configProxy.getClass().getInterfaces()));
	}
	
	public List<Class<?>> getAllConfigInterfaces()
	{
		return configInterfaces;
	}
	
	public <T> T getConfigObject(Class<T> configInterface) throws ConfigSetException
	{
		if (configInterfaces.contains(configInterface))
			return configInterface.cast(this.configProxy);
		throw new ConfigSetException("This ConfigSet doesn't contain configuration for '" + configInterface.getName() + "'.");
	}
}
