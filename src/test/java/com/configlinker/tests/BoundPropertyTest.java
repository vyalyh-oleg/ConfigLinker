package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundPropertyTest extends AbstractBaseTest
{
	private static List<String> languagesInConfigFile;
	private static List<String> languagesInConfigFile_withSpaces;
	private static Map<String, Double> languageScoresInConfigFile;
	private static Map<String, Double> languageScoresInConfigFile_withSpaces;
	
	@BeforeAll
	static void initHardcodedValues()
	{
		ArrayList<String> langs = new ArrayList<>();
		langs.add("Java");
		langs.add("Python");
		langs.add("JavaScript");
		langs.add("C");
		langs.add("C++");
		langs.add("C#");
		langs.add("PHP");
		BoundPropertyTest.languagesInConfigFile = Collections.unmodifiableList(langs);
		
		ArrayList<String> langsWithSpaces = new ArrayList<>();
		langsWithSpaces.add("Java");
		langsWithSpaces.add(" Python ");
		langsWithSpaces.add(" JavaScript");
		langsWithSpaces.add(" C ");
		langsWithSpaces.add(" C++");
		langsWithSpaces.add(" C# ");
		langsWithSpaces.add(" PHP ");
		BoundPropertyTest.languagesInConfigFile_withSpaces = Collections.unmodifiableList(langsWithSpaces);
		
		LinkedHashMap<String, Double> langScores = new LinkedHashMap<>();
		langScores.put("Java", 14.941);
		langScores.put("C", 12.760);
		langScores.put("C++", 6.452);
		langScores.put("Python", 5.869);
		langScores.put("C#", 5.067);
		langScores.put("PHP", 4.010);
		langScores.put("JavaScript", 3.916);
		BoundPropertyTest.languageScoresInConfigFile = Collections.unmodifiableMap(langScores);
		
		LinkedHashMap<String, Double> langScoresWithWhitespaces = new LinkedHashMap<>();
		langScoresWithWhitespaces.put(" Java ", 14.941);
		langScoresWithWhitespaces.put("C", 12.760);
		langScoresWithWhitespaces.put(" C++  ", 6.452);
		langScoresWithWhitespaces.put(" Python", 5.869);
		langScoresWithWhitespaces.put(" C#", 5.067);
		langScoresWithWhitespaces.put(" PHP ", 4.010);
		langScoresWithWhitespaces.put("JavaScript", 3.916);
		BoundPropertyTest.languageScoresInConfigFile_withSpaces = Collections.unmodifiableMap(langScoresWithWhitespaces);
	}
	
	
	@Test
	void test_customDelimiterForList()
	{
		BoundPropFunc_customListDelimiter langList = this.getSingleConfigInstance(BoundPropFunc_customListDelimiter.class);
		Assertions.assertEquals(BoundPropertyTest.languagesInConfigFile, langList.programmingLanguages());
	}
	
	@Test
	void test_customDelimiterForListAndAcceptWhitespaces()
	{
		BoundPropFunc_customListDelimiter_acceptWhitespaces langList = this.getSingleConfigInstance(BoundPropFunc_customListDelimiter_acceptWhitespaces.class);
		Assertions.assertEquals(BoundPropertyTest.languagesInConfigFile_withSpaces, langList.programmingLanguages());
	}
	
	@Test
	void test_customDelimiterForMap()
	{
		BoundPropFunc_customKVDelimiter langMap = this.getSingleConfigInstance(BoundPropFunc_customKVDelimiter.class);
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile, langMap.programmingLanguageScores());
	}
	
	@Test
	void test_customDelimiterForMapAndAcceptWhitespaces()
	{
		BoundPropFunc_customKVDelimiter_acceptWhitespaces langMap = this.getSingleConfigInstance(BoundPropFunc_customKVDelimiter_acceptWhitespaces.class);
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile_withSpaces, langMap.programmingLanguageScores());
	}
	
	@Test
	void test_whitespaceInheritance()
	{
		BoundPropFunc_customKVDelimiter_whitespaceInheritance langMap = this
		  .getSingleConfigInstance(BoundPropFunc_customKVDelimiter_whitespaceInheritance.class);
		
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile, langMap.programmingLanguageScores());
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile_withSpaces, langMap.programmingLanguageScores_acceptWhitespaces());
	}
	
	@Test
	void test_regexValidation()
	{
		String regex2= "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
		BoundPropFunc_regexValidator regexValidatorProperty = this.getSingleConfigInstance(BoundPropFunc_regexValidator.class);
		BoundPropFunc_regexValidator_withError regexValidatorProperty_withError = this.getSingleConfigInstance(BoundPropFunc_regexValidator_withError.class);
		
	}
	
	@Test @Disabled("TODO: implement")
	void test_customValidator()
	{
		//TODO: implement
	}
	
	@Test @Disabled("TODO: implement")
	void test_errorBehaviour()
	{
		//TODO: implement
	}
	
	// TODO: inheritance
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customListDelimiter
{
	@BoundProperty(name = "programming.languages", customTypeOrDeserializer = String.class, delimiterForList = ",,")
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customListDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages", customTypeOrDeserializer = String.class, delimiterForList = ",,", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customKVDelimiter
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customKVDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customKVDelimiter_whitespaceInheritance
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
	
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores_acceptWhitespaces();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_regexValidator
{
	@BoundProperty(name = "workgroup.name", regexPattern = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_regexValidator_withError
{
	@BoundProperty(name = "workgroup.name.error", regexPattern = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customValidator
{

}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_errorBehavior
{

}
