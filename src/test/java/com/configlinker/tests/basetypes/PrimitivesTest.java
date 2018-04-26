package com.configlinker.tests.basetypes;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMapException;
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
		
		Assertions.assertEquals("wrong", primitiveBoolean.getWrongValueAsString());
		Assertions.assertFalse(primitiveBoolean.getWrongValue());
	}
	
	@Test
	void test_primitiveByte()
	{
		PrimitiveByte primitiveByte = getSingleConfigInstance(PrimitiveByte.class);
		
		Assertions.assertEquals("100", primitiveByte.getValueFromString());
		Assertions.assertEquals(100, primitiveByte.getValue());
	}
	
	@Test
	void test_primitiveByteError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			PrimitiveByteError primitiveByteError = getSingleConfigInstance(PrimitiveByteError.class);
			Assertions.fail("This should be unreachable code point.");
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.lang.Byte::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(baseCause.getClass(), NumberFormatException.class);
		Assertions.assertEquals("Value out of range. Value:\"128\" Radix:10", baseCause.getMessage());
	}
	
	@Test
	void test_primitiveChar()
	{
		PrimitiveChar primitiveChar = getSingleConfigInstance(PrimitiveChar.class);
		
		Assertions.assertEquals("r", primitiveChar.getValueAsString());
		Assertions.assertEquals('r', primitiveChar.getValue());
	}
	
	@Test
	void test_primitiveCharError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			PrimitiveCharError primitiveCharError = getSingleConfigInstance(PrimitiveCharError.class);
			System.out.println("test_primitiveCharError" + primitiveCharError.getWrongValue());
			Assertions.fail("This should be unreachable code point.");
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.lang.Byte::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(baseCause.getClass(), NumberFormatException.class);
		Assertions.assertEquals("Value out of range. Value:\"128\" Radix:10", baseCause.getMessage());
	}
	
	@Test
	void test_primitiveShort()
	{
		PrimitiveShort primitiveShort = getSingleConfigInstance(PrimitiveShort.class);
		
		Assertions.assertEquals("30802", primitiveShort.getValueAsString());
		Assertions.assertEquals((short) 30802, primitiveShort.getValue());
	}
	
	@Test
	void test_primitiveShortError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			PrimitiveShortError primitiveShortError = getSingleConfigInstance(PrimitiveShortError.class);
			Assertions.fail("This should be unreachable code point.");
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.lang.Short::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(baseCause.getClass(), NumberFormatException.class);
		Assertions.assertEquals("Value out of range. Value:\"-33000\" Radix:10", baseCause.getMessage());
	}
	
	@Test
	void test_primitiveInt()
	{
		PrimitiveInt primitiveInt = getSingleConfigInstance(PrimitiveInt.class);
		
		Assertions.assertEquals("2045968711", primitiveInt.getIntFromDecimalAsString());
		Assertions.assertEquals(2045968711, primitiveInt.getIntFromDecimal());
	}
	
	@Test
	void test_primitiveIntError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			PrimitiveIntError primitiveIntError = getSingleConfigInstance(PrimitiveIntError.class);
			Assertions.fail("This should be unreachable code point.");
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.lang.Integer::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(baseCause.getClass(), NumberFormatException.class);
		Assertions.assertEquals("For input string: \"2245968711\"", baseCause.getMessage());
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
	
	@BoundProperty(name = "type.boolean.wrong")
	boolean getWrongValue();
	
	@BoundProperty(name = "type.boolean.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveByte
{
	@BoundProperty(name = "type.byte")
	byte getValue();
	
	@BoundProperty(name = "type.byte")
	String getValueFromString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveByteError
{
	@BoundProperty(name = "type.byte.wrong")
	byte getWrongValue();
	
	@BoundProperty(name = "type.byte.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveChar
{
	@BoundProperty(name = "type.char")
	char getValue();
	
	@BoundProperty(name = "type.char")
	String getValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveCharError
{
	@BoundProperty(name = "type.char.wrong")
	char getWrongValue();
	
	@BoundProperty(name = "type.char.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveShort
{
	@BoundProperty(name = "type.short")
	short getValue();
	
	@BoundProperty(name = "type.short")
	String getValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveShortError
{
	@BoundProperty(name = "type.short.wrong")
	short getWrongValue();
	
	@BoundProperty(name = "type.short.wrong")
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

@BoundObject(sourcePath = "./configs/base_types.properties")
interface PrimitiveIntError
{
	@BoundProperty(name = "type.int.dec.wrong")
	int getIntFromDecimal();
	
	@BoundProperty(name = "type.int.dec.wrong")
	String getIntFromDecimalAsString();
}

