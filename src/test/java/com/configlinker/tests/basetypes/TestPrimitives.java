package com.configlinker.tests.basetypes;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


class TestPrimitives
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
	
	@Test
	void test_primitiveBoolean()
	{
		PrimitiveBoolean primitiveBoolean = getSingleConfigInstance(PrimitiveBoolean.class);
		Assertions.assertEquals("true", primitiveBoolean.getTrueAsString());
		Assertions.assertTrue(primitiveBoolean.getTrue());
		Assertions.assertEquals("false", primitiveBoolean.getFalseAsString());
		Assertions.assertFalse(primitiveBoolean.getFalse());
	}
	
	@Test
	void test_primitiveBooleanException()
	{
		PrimitiveBooleanError primitiveBooleanError = getSingleConfigInstance(PrimitiveBooleanError.class);
		Assertions.fail("test fail");
	}
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveBoolean
{
	@BoundProperty(name = "type.boolean.true")
	boolean getTrue();
	
	@BoundProperty(name = "type.boolean.true")
	String getTrueAsString();
	
	@BoundProperty(name = "type.boolean.false")
	boolean getFalse();
	
	@BoundProperty(name = "type.boolean.false")
	String getFalseAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveBooleanError
{
	@BoundProperty(name = "type.boolean.error")
	boolean getWrongValue();
	
	@BoundProperty(name = "type.boolean.error")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveInt
{
	@BoundProperty(name = "type.int.dec")
	int getIntFromDecimal();
	
	@BoundProperty(name = "type.int.dec")
	String getIntFromDecimalAsString();
}
