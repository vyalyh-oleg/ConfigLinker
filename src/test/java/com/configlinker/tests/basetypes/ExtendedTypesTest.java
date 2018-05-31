package com.configlinker.tests.basetypes;


import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ExtendedTypesTest
{
	private ConfigSet getConfigSet(Class<?>... interfaceType)
	{
		Set<Class<?>> configClasses = new HashSet<>(Arrays.asList(interfaceType));
		return ConfigSetFactory.create(configClasses);
	}
	
	private <T> T getSingleConfigInstance(Class<T> interfaceType)
	{
		return getConfigSet(interfaceType).getConfigObject(interfaceType);
	}
	
	
}
