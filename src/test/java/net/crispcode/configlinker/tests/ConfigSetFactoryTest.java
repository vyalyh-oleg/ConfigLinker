package net.crispcode.configlinker.tests;


import net.crispcode.configlinker.ConfigSet;
import net.crispcode.configlinker.ConfigSetFactory;
import net.crispcode.configlinker.exceptions.ConfigSetException;
import org.junit.jupiter.api.Assertions;
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
	
	@Test
	void test_factoryShouldFailIfAttemptsToReceiveAbsentInterface()
	{
		ConfigSet configSet = ConfigSetFactory.create(NonEmptyValue.class);
		try
		{
			AnotherNonEmptyValue configuration = configSet.getConfigObject(AnotherNonEmptyValue.class);
			Assertions.fail("AnotherNonEmptyValue configuration shouldn't have been found.");
		}
		catch (ConfigSetException e)
		{
			Assertions.assertEquals("This ConfigSet doesn't contain configuration for 'net.crispcode.configlinker.tests.AnotherNonEmptyValue'.", e.getMessage());
		}
	}
	
	// If the return type is array, it will be always return the same array, so the array object cannot be used as unmodifiable (immutable representations of set of properties).
	// Use List, Set or Map instead.
}
