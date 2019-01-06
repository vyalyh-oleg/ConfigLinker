package com.configlinker.tests;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundObjectTrackChangesTest extends AbstractBaseTest
{
	@Test
	void someTest()
	{
		// httpHeaders
		// trackPolicy
		// trackInterval
		// changeListener
		//FactorySettingsBuilder.create().setListener
	}
}

@BoundObject(sourcePath = "./configs/bound_object_functionality.properties")
interface SomeProperty
{
	@BoundProperty(name = "name")
	String value();
}