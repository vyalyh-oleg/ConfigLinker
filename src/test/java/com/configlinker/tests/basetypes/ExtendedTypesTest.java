package com.configlinker.tests.basetypes;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.tests.AbstractBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ExtendedTypesTest extends AbstractBaseTest
{
	@Test
	void test_typeEnum()
	{
		TypeEnum typeBoolean = getSingleConfigInstance(TypeEnum.class);
		
		Assertions.assertEquals(NumberNames.three, typeBoolean.getNumber());
		Assertions.assertEquals(NumberNames.three.name(), typeBoolean.getNumberAsString());
	}
	
	@Test
	void test_typeEnumError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeEnumError typeBoolean = getSingleConfigInstance(TypeEnumError.class);
			Assertions.fail("This should be unreachable code point.");
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'com.configlinker.tests.basetypes.NumberNames::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(IllegalArgumentException.class, baseCause.getClass());
		Assertions.assertEquals("No enum constant com.configlinker.tests.basetypes.NumberNames.eleven", baseCause.getMessage());
	}
	
}


enum NumberNames
{
	one, two, three, four, five
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeEnum
{
	@BoundProperty(name = "type.Enum.numberName")
	NumberNames getNumber();
	
	@BoundProperty(name = "type.Enum.numberName")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeEnumError
{
	@BoundProperty(name = "type.Enum.numberName.wrong")
	NumberNames getNumber();
	
	@BoundProperty(name = "type.Enum.numberName.wrong")
	String getNumberAsString();
}