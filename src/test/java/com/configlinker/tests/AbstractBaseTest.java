package com.configlinker.tests;


import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.FactoryConfigBuilder;

import java.util.Set;


public abstract class AbstractBaseTest
{
	protected ConfigSet getConfigSet(FactoryConfigBuilder configBuilder, Set<Class<?>> configInterfaces)
	{
		if (configBuilder == null)
			return ConfigSetFactory.create(configInterfaces);
		else
			return ConfigSetFactory.create(configBuilder, configInterfaces);
	}
	
	protected <T> T getSingleConfigInstance(Class<T> interfaceType)
	{
		return ConfigSetFactory.create(interfaceType).getConfigObject(interfaceType);
	}
	
	protected <T> T getSingleConfigInstance(FactoryConfigBuilder configBuilder, Class<T> interfaceType)
	{
		return ConfigSetFactory.create(configBuilder, interfaceType).getConfigObject(interfaceType);
	}
}
