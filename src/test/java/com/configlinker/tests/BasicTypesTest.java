package com.configlinker.tests;

import com.configlinker.FactorySettingsBuilder;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.Whitespaces;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BasicTypesTest extends AbstractBaseTest
{
	@Test
	void test_typeBoolean()
	{
		TypeBoolean typeBoolean = getSingleConfigInstance(TypeBoolean.class);
		
		Assertions.assertEquals("true", typeBoolean.getTrueAsString());
		Assertions.assertTrue(typeBoolean.getTrue());
		
		Assertions.assertEquals("false", typeBoolean.getFalseAsString());
		Assertions.assertFalse(typeBoolean.getFalse());
		
		Assertions.assertEquals("True", typeBoolean.getTrue2AsString());
		Assertions.assertTrue(typeBoolean.getTrue2());
		
		Assertions.assertEquals("False", typeBoolean.getFalse2AsString());
		Assertions.assertFalse(typeBoolean.getFalse2());
		
		Assertions.assertEquals("wrong", typeBoolean.getWrongValueAsString());
		Assertions.assertFalse(typeBoolean.getWrongValue());
	}
	
	@Test
	void test_typeByte()
	{
		TypeByte typeByte = getSingleConfigInstance(TypeByte.class);
		
		Assertions.assertEquals("100", typeByte.getValueFromString());
		Assertions.assertEquals(100, typeByte.getValue());
	}
	
	@Test
	void test_typeByteError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeByteError typeByteError = getSingleConfigInstance(TypeByteError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Byte::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("Value out of range. Value:\"128\" Radix:10", baseCause.getMessage());
	}
	
	@Test
	void test_typeChar()
	{
		TypeChar typeChar = getSingleConfigInstance(TypeChar.class);
		
		Assertions.assertEquals("r", typeChar.getValueAsString());
		Assertions.assertEquals('r', typeChar.getValue());
	}
	
	@Test
	void test_typeCharError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeCharError typeCharError = getSingleConfigInstance(TypeCharError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'com.configlinker.mappers.CharacterMapper::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(IllegalArgumentException.class, baseCause.getClass());
		Assertions.assertEquals("Given string 'rb' instead of a single char.", baseCause.getMessage());
	}
	
	@Test
	void test_typeShort()
	{
		TypeShort typeShort = getSingleConfigInstance(TypeShort.class);
		
		Assertions.assertEquals("30802", typeShort.getValueAsString());
		Assertions.assertEquals((short) 30802, typeShort.getValue());
	}
	
	@Test
	void test_typeShortError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeShortError typeShortError = getSingleConfigInstance(TypeShortError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Short::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("Value out of range. Value:\"-33000\" Radix:10", baseCause.getMessage());
	}
	
	@Test
	void test_typeInt()
	{
		TypeInt typeInt = getSingleConfigInstance(TypeInt.class);
		
		Assertions.assertEquals("2045968711", typeInt.getIntFromDecimalAsString());
		Assertions.assertEquals(2045968711, typeInt.getIntFromDecimal());
	}
	
	@Test
	void test_typeIntError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeIntError typeIntError = getSingleConfigInstance(TypeIntError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Integer::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"2245968711\"", baseCause.getMessage());
	}
	
	@Test
	void test_typeLong()
	{
		TypeLong typeLong = getSingleConfigInstance(TypeLong.class);
		
		Assertions.assertEquals("1234567890987654321", typeLong.getValueAsString());
		Assertions.assertEquals(1234567890987654321L, typeLong.getValue());
	}
	
	@Test
	void test_typeLongError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeLongError typeLongError = getSingleConfigInstance(TypeLongError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Long::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"19234567890987654321\"", baseCause.getMessage());
	}
	
	
	@Test
	void test_typeFloat()
	{
		TypeFloat typeFloat = getSingleConfigInstance(TypeFloat.class);
		
		Assertions.assertEquals("0.000103", typeFloat.getValueAsString());
		Assertions.assertEquals(0.000103F, typeFloat.getValue());
		Assertions.assertEquals("0.000103e-14", typeFloat.getValueWithExponentAsString());
		Assertions.assertEquals(0.000103e-14F, typeFloat.getValueWithExponent());
	}
	
	@Test
	void test_typeFloatError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeFloatError typeFloatError = getSingleConfigInstance(TypeFloatError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Float::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"2.04e+!13\"", baseCause.getMessage());
	}
	
	@Test
	void test_typeDouble()
	{
		TypeDouble typeDouble = getSingleConfigInstance(TypeDouble.class);
		
		Assertions.assertEquals("2.040332982365", typeDouble.getValueAsString());
		Assertions.assertEquals(2.040332982365D, typeDouble.getValue());
		Assertions.assertEquals("2.040332e-101", typeDouble.getValueWithExponentAsString());
		Assertions.assertEquals(2.040332e-101D, typeDouble.getValueWithExponent());
	}
	
	@Test
	void test_typeDoubleError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeDoubleError typeDoubleError = getSingleConfigInstance(TypeDoubleError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.lang.Double::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"2.040332-101\"", baseCause.getMessage());
	}
	
	@Test
	void test_typeMultilineString()
	{
		TypeMultilineString typeMultilineString = getSingleConfigInstance(TypeMultilineString.class);
		// Take into account that default value for FactorySettingsBuilder.whitespaces() == Whitespaces.IGNORE
		Assertions.assertEquals("Just a simple multiline text", typeMultilineString.getValue1());
		Assertions.assertEquals("Just a simple multiline text", typeMultilineString.getValue2());
		Assertions.assertEquals("Just a simplemultiline text", typeMultilineString.getValue3());
		Assertions.assertEquals("Just a simple \n multiline text", typeMultilineString.getValue4());
		Assertions.assertEquals("Just a simple \r multiline text", typeMultilineString.getValue5());
		Assertions.assertEquals("Just a simple b multiline text", typeMultilineString.getValue6());
		Assertions.assertEquals("Just a simple \f multiline text", typeMultilineString.getValue7());
	}
	
	@Test
	void test_typeMultilineString_withWhitespaces()
	{
		TypeMultilineString typeMultilineString = getSingleConfigInstance(FactorySettingsBuilder.create().setWhitespaces(Whitespaces.ACCEPT), TypeMultilineString.class);
		Assertions.assertEquals("Just a simple multiline text", typeMultilineString.getValue1());
		Assertions.assertEquals("Just a simple multiline text", typeMultilineString.getValue2());
		Assertions.assertEquals("Just a simplemultiline text   ", typeMultilineString.getValue3()); // 3 trailing spaces
		Assertions.assertEquals("Just a simple \n multiline text", typeMultilineString.getValue4());
		Assertions.assertEquals("Just a simple \r multiline text  ", typeMultilineString.getValue5()); // 2 trailing spaces
		Assertions.assertEquals(" Just a simple b multiline text   ", typeMultilineString.getValue6()); // 1 leading space and 3 trailing spaces
		Assertions.assertEquals("Just a simple \f multiline text", typeMultilineString.getValue7());
	}
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeBoolean
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
interface TypeByte
{
	@BoundProperty(name = "type.byte")
	byte getValue();
	
	@BoundProperty(name = "type.byte")
	String getValueFromString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeByteError
{
	@BoundProperty(name = "type.byte.wrong")
	byte getWrongValue();
	
	@BoundProperty(name = "type.byte.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeChar
{
	@BoundProperty(name = "type.char")
	char getValue();
	
	@BoundProperty(name = "type.char")
	String getValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeCharError
{
	@BoundProperty(name = "type.char.wrong")
	char getWrongValue();
	
	@BoundProperty(name = "type.char.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeShort
{
	@BoundProperty(name = "type.short")
	short getValue();
	
	@BoundProperty(name = "type.short")
	String getValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeShortError
{
	@BoundProperty(name = "type.short.wrong")
	short getWrongValue();
	
	@BoundProperty(name = "type.short.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeInt
{
	@BoundProperty(name = "type.int.dec")
	int getIntFromDecimal();
	
	@BoundProperty(name = "type.int.dec")
	String getIntFromDecimalAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeIntError
{
	@BoundProperty(name = "type.int.dec.wrong")
	int getWrongIntFromDecimal();
	
	@BoundProperty(name = "type.int.dec.wrong")
	String getWrongIntFromDecimalAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeLong
{
	@BoundProperty(name = "type.long")
	long getValue();
	
	@BoundProperty(name = "type.long")
	String getValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeLongError
{
	@BoundProperty(name = "type.long.wrong")
	long getWrongValue();
	
	@BoundProperty(name = "type.long.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeFloat
{
	@BoundProperty(name = "type.float")
	float getValue();
	
	@BoundProperty(name = "type.float")
	String getValueAsString();
	
	@BoundProperty(name = "type.float.exponent")
	float getValueWithExponent();
	
	@BoundProperty(name = "type.float.exponent")
	String getValueWithExponentAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeFloatError
{
	@BoundProperty(name = "type.float.wrong")
	float getWrongValue();
	
	@BoundProperty(name = "type.float.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeDouble
{
	@BoundProperty(name = "type.double")
	double getValue();
	
	@BoundProperty(name = "type.double")
	String getValueAsString();
	
	@BoundProperty(name = "type.double.exponent")
	double getValueWithExponent();
	
	@BoundProperty(name = "type.double.exponent")
	String getValueWithExponentAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeDoubleError
{
	@BoundProperty(name = "type.double.wrong")
	double getWrongValue();
	
	@BoundProperty(name = "type.double.wrong")
	String getWrongValueAsString();
}

@BoundObject(sourcePath = "./configs/base_types.properties")
interface TypeMultilineString
{
	@BoundProperty(name = "type.String.1")
	String getValue1();
	
	@BoundProperty(name = "type.String.2")
	String getValue2();
	
	@BoundProperty(name = "type.String.3")
	String getValue3();
	
	@BoundProperty(name = "type.String.4")
	String getValue4();
	
	@BoundProperty(name = "type.String.5")
	String getValue5();
	
	@BoundProperty(name = "type.String.6")
	String getValue6();
	
	@BoundProperty(name = "type.String.7")
	String getValue7();
}
