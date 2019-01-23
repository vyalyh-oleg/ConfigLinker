package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PropertyVariableSubstitutionTest extends AbstractBaseTest
{
	// propertyNamePrefix in BoundObject
	
	@Test
	void test1()
	{
	
	}
}


enum Info
{
	priority, designed, birthday;
}

enum Lang
{
	Java, Python, JavaScript, C, Cpp, Csh, PHP;
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.languages")
interface Prop_ValueWithPrefix
{
	@BoundProperty(name = ".java.priority")
	String value();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.languages")
interface Prop_ValueWithAndWithoutPrefix
{
	@BoundProperty(name = ".java.priority")
	String value();
	
	@BoundProperty(name = "programming.languages.java.designed")
	String value2();
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties")
interface Prop_SourcePathWithVar
{
	@BoundProperty(name = "programming.languages")
	List<Lang> languages();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.${part}")
interface Prop_PrefixWithVar
{
	@BoundProperty(name = ".java.priority")
	String value();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties")
interface Prop_ValueWithVar
{
	@BoundProperty(name = "programming.languages.${lang}.designed")
	String value();
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "programming.${part}")
interface Prop_SourcePathWithVar_PrefixWithVar_ValuesWithVar_ValueWithAndWithoutPrefix
{
	@BoundProperty(name = ".${lang}.priority")
	String value();
	
	@BoundProperty(name = "programming.languages.${lang}.designed")
	String value2();
}


@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "${globalPrefix}")
interface DynamicPropString_VarInAllParts
{
	@BoundProperty(name = ".${part}.@{langName}.@{info}")
	String langInfo(String langName, String info);
	
	@BoundProperty(name = "programming.${part}.@{langName}.designed")
	String langAuthor(String langName);
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "${globalPrefix}")
interface DynamicPropStringEnum_VarInAllParts
{
	@BoundProperty(name = ".${part}.@{langName}.@{info}")
	String langInfo(String langName, Info info);
}
