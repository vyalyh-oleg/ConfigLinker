package com.configlinker.tests.basetypes;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PrimitivesTest
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
		
		Assertions.assertEquals("True", primitiveBoolean.getTrue2AsString());
		Assertions.assertTrue(primitiveBoolean.getTrue2());
		
		Assertions.assertEquals("False", primitiveBoolean.getFalse2AsString());
		Assertions.assertFalse(primitiveBoolean.getFalse2());
		
		Assertions.assertEquals("error", primitiveBoolean.getWrongValueAsString());
		Assertions.assertFalse(primitiveBoolean.getWrongValue());
	}
	
	@Test
	void test_primitiveByte()
	{
	
	}
	
	@Test
	void test_primitiveByteError()
	{
	
	}
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveBoolean
{
	@BoundProperty(name = "type.boolean.true")
	boolean getTrue();
	
	@BoundProperty(name = "type.boolean.true")
	String getTrueAsString();
	
	@BoundProperty(name = "type.boolean.True")
	boolean getTrue2();
	
	@BoundProperty(name = "type.boolean.True")
	String getTrue2AsString();
	
	@BoundProperty(name = "type.boolean.false")
	boolean getFalse();
	
	@BoundProperty(name = "type.boolean.false")
	String getFalseAsString();
	
	@BoundProperty(name = "type.boolean.False")
	boolean getFalse2();
	
	@BoundProperty(name = "type.boolean.False")
	String getFalse2AsString();
	
	@BoundProperty(name = "type.boolean.error")
	boolean getWrongValue();
	
	@BoundProperty(name = "type.boolean.error")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveByte
{
	@BoundProperty(name = "type.byte")
	int getFromDecimal();
	
	@BoundProperty(name = "type.byte")
	String getFromDecimalAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveByteError
{
	@BoundProperty(name = "type.byte.wrong")
	int getFromDecimalWrongValue();
	
	@BoundProperty(name = "type.byte.wrong")
	int getFromDecimalAsStringWrongValue();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveInt
{
	@BoundProperty(name = "type.int.dec")
	int getIntFromDecimal();
	
	@BoundProperty(name = "type.int.dec")
	String getIntFromDecimalAsString();
}
