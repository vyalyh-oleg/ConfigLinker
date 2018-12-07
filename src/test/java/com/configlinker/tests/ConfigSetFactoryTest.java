package com.configlinker.tests;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ConfigSetFactoryTest
{
	@Test
	void test_factoryProduceSameConfigInstances()
	{
		// the ConfigSets, produced by the factory, are the same for every call
		
	}


}


