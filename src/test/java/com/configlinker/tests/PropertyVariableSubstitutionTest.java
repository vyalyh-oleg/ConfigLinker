package com.configlinker.tests;


import com.configlinker.FactorySettingsBuilder;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.deserializers.DateType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;
import java.util.List;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PropertyVariableSubstitutionTest extends AbstractBaseTest
{
	@Test
	void test_valueWithPrefix()
	{
		Prop_ValueWithPrefix properties = getSingleConfigInstance(Prop_ValueWithPrefix.class);
		
	}
	
	@Test
	void test_valueWithAndWithoutPrefix()
	{
		Prop_ValueWithAndWithoutPrefix properties = getSingleConfigInstance(Prop_ValueWithAndWithoutPrefix.class);
		
	}
	
	@Test
	void test_sourcePathWithVar()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create().addParameter("subfolder", "config");
		Prop_SourcePathWithVar properties = getSingleConfigInstance(builder, Prop_SourcePathWithVar.class);
		
	}
	
	@Test
	void test_prefixWithVar()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create().addParameter("part", "languages");
		Prop_PrefixWithVar properties = getSingleConfigInstance(builder, Prop_PrefixWithVar.class);
		
		
	}
	
	@Test
	void test_valueWithVar()
	{
		FactorySettingsBuilder builderC = FactorySettingsBuilder.create().addParameter("lang", "C");
		Prop_ValueWithVar propertiesC = getSingleConfigInstance(builderC, Prop_ValueWithVar.class);
		
		FactorySettingsBuilder builderCpp = FactorySettingsBuilder.create().addParameter("lang", "Cpp");
		Prop_ValueWithVar propertiesCpp = getSingleConfigInstance(builderCpp, Prop_ValueWithVar.class);
		
		FactorySettingsBuilder builderCsh = FactorySettingsBuilder.create().addParameter("lang", "Csh");
		Prop_ValueWithVar propertiesCsh = getSingleConfigInstance(builderCsh, Prop_ValueWithVar.class);
		
	}
	
	@Test
	void test_sourcePathWithVar_PrefixWithVar_ValuesWithVar_ValueWithAndWithoutPrefix()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "config")
			.addParameter("part", "languages")
			.addParameter("lang", "PHP");
		Prop_VarInAllParts properties = getSingleConfigInstance(builder, Prop_VarInAllParts.class);
		
		
	}
	
	@Test
	void test_dynamicPropString_VarInAllParts()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "config")
			.addParameter("globalPrefix", "programming")
			.addParameter("lang", "PHP");
		DynamicPropString_VarInAllParts properties = getSingleConfigInstance(builder, DynamicPropString_VarInAllParts.class);
		
		
	}
	
	@Test
	void test_dynamicPropStringEnum_VarInAllParts()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "config")
			.addParameter("globalPrefix", "programming")
			.addParameter("part", "languages");
		DynamicPropStringEnum_VarInAllParts properties = getSingleConfigInstance(builder, DynamicPropStringEnum_VarInAllParts.class);
		
		
	}
	
	@Test
	void test_dynamicProp_VarInAllParts_TrackChanges()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "config")
			.addParameter("globalPrefix", "programming")
			.addParameter("part", "languages");
		DynamicProp_VarInAllParts_TrackChanges properties = getSingleConfigInstance(builder, DynamicProp_VarInAllParts_TrackChanges.class);
		
		
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
	int javaPriority();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.languages")
interface Prop_ValueWithAndWithoutPrefix
{
	@BoundProperty(name = ".java.priority")
	int cPriority();
	
	@BoundProperty(name = "programming.languages.java.designed")
	String cAuthor();
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
	@BoundProperty(name = ".python.priority")
	int pythonPriority();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties")
interface Prop_ValueWithVar
{
	@BoundProperty(name = "programming.languages.${lang}.designed")
	String langAuthor();
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "programming.${part}")
interface Prop_VarInAllParts
{
	@BoundProperty(name = ".${lang}.priority")
	int langPriority();
	
	@BoundProperty(name = "programming.${part}.${lang}.birthday", customType = DateType.DateOnly.class)
	Date langBirthday();
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
	@BoundProperty(name = ".${part}.@{lang}.@{info}")
	String langInfo(Lang lang, Info info);
}


@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "${globalPrefix}")
interface DynamicProp_VarInAllParts_TrackChanges
{
	@BoundProperty(name = ".${part}.@{lang}.@{info}")
	String langInfo(Lang lang, Info info);
	
	@BoundProperty(name = "programming.${part}.@{langName}.designed")
	String langAuthor(String langName);
}
