package com.configlinker.tests;


import com.configlinker.FactorySettingsBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class FactorySettingsBuilderTest extends AbstractBaseTest
{
	@Test
	void test_defaultParameters()
	{
		FactorySettingsBuilder.create();
	}
	
	@Test
	void test_setAndGetMethods()
	{
		FactorySettingsBuilder.create();
	}
	
	@Test
	void test_builderCloseAfterUsage()
	{
		FactorySettingsBuilder.create();
	}
}
