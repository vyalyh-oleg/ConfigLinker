package com.configlinker.tests;


import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.FactorySettingsBuilder;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;
import java.util.logging.LogManager;


public abstract class AbstractBaseTest
{
	@BeforeAll
	public static void setupBeforeAll()
	{
		LogManager.getLogManager().getLogger("").getHandlers();
		
		// Optionally remove existing handlers attached to j.u.l root logger
		// SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)
		
		// add SLF4JBridgeHandler to j.u.l's root logger
		// should be done once during the initialization phase of your application
		// SLF4JBridgeHandler.install();
	}
	
	protected ConfigSet getConfigSet(Class<?>... configInterfaces)
	{
		return ConfigSetFactory.create(configInterfaces);
	}
	
	protected ConfigSet getConfigSet(FactorySettingsBuilder factorySettingsBuilder, Class<?>... configInterfaces)
	{
		return ConfigSetFactory.create(factorySettingsBuilder, configInterfaces);
	}
	
	protected ConfigSet getConfigSet(Set<Class<?>> configInterfaces)
	{
		return ConfigSetFactory.create(configInterfaces);
	}
	
	protected ConfigSet getConfigSet(FactorySettingsBuilder factorySettingsBuilder, Set<Class<?>> configInterfaces)
	{
		return ConfigSetFactory.create(factorySettingsBuilder, configInterfaces);
	}
	
	protected <T> T getSingleConfigInstance(Class<T> interfaceType)
	{
		return ConfigSetFactory.create(interfaceType).getConfigObject(interfaceType);
	}
	
	protected <T> T getSingleConfigInstance(FactorySettingsBuilder factorySettingsBuilder, Class<T> interfaceType)
	{
		return ConfigSetFactory.create(factorySettingsBuilder, interfaceType).getConfigObject(interfaceType);
	}
}
