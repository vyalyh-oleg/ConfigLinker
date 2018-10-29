package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundPropertyTest extends AbstractBaseTest
{
	@Test @Disabled("TODO: implement")
	void test_customDelimiterForList()
	{
	
	}
	
	@Test @Disabled("TODO: implement")
	void test_customDelimiterForMap()
	{
	
	}
	
	@Test @Disabled("TODO: implement")
	void test_regexValidation()
	{
	
	}
	
	@Test @Disabled("TODO: implement")
	void test_customValidator()
	{
	
	}
	
	@Test @Disabled("TODO: implement")
	void test_errorBehaviour()
	{
	
	}
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc
{
	@BoundProperty(name = "programming.languages", delimiterForList = ",,")
	List<String> programmingLanguages();
}