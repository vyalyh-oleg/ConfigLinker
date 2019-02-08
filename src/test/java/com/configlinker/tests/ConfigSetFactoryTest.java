package com.configlinker.tests;


import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ConfigSetFactoryTest extends AbstractBaseTest
{
	@Test
	void test_factoryProduceSameConfigInstances()
	{
		ConfigSet configSet = ConfigSetFactory.create(NonEmptyValue.class);
		Assertions.assertSame(configSet.getConfigObject(NonEmptyValue.class), configSet.getConfigObject(NonEmptyValue.class));
	}
	
	@Test
	void test_configInterfaceReturnsSameParameterFromInstance()
	{
		NonEmptyValue nonEmptyValue = this.getSingleConfigInstance(NonEmptyValue.class);
		Assertions.assertSame(nonEmptyValue.non_empty(), nonEmptyValue.non_empty());
	}
	
	@Test @Disabled("TODO: implement")
	void test_factoryShouldFailIfAttemptsToReceiveAbsentInterface()
	{
		//TODO: implement test_factoryShouldFailIfAttemptsToReceiveAbsentInterface
	}
	
	// If the return type is array, it will be always return the same array, so the array object cannot be used as unmodifiable (immutable representations of set of properties).
	// Use List, Set or Map instead.
}
