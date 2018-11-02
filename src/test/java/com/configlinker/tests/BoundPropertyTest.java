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


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundPropertyTest extends AbstractBaseTest
{
	private List<String> languagesInConfigFile;
	private List<String> languagesInConfigFile_withSpaces;
	private Map<String, Double> languageScoresInConfigFile;
	private Map<String, Double> languageScoresInConfigFile_withSpaces;
	
	@BeforeAll
	void initHardcodedValues()
	{
		ArrayList<String> langs = new ArrayList<>();
		langs.add("Java");
		langs.add("Python");
		langs.add("JavaScript");
		langs.add("C");
		langs.add("C++");
		langs.add("C#");
		langs.add("PHP");
		languagesInConfigFile = Collections.unmodifiableList(langs);
		
		ArrayList<String> langsWithSpaces = new ArrayList<>();
		langsWithSpaces.add("Java");
		langsWithSpaces.add(" Python ");
		langsWithSpaces.add(" JavaScript");
		langsWithSpaces.add(" C ");
		langsWithSpaces.add(" C++");
		langsWithSpaces.add(" C# ");
		langsWithSpaces.add(" PHP ");
		languagesInConfigFile_withSpaces = Collections.unmodifiableList(langsWithSpaces);
		
		LinkedHashMap<String, Double> langScores = new LinkedHashMap<>();
		langScores.put("Java", 14.941);
		langScores.put("C", 12.760);
		langScores.put("C++", 6.452);
		langScores.put("Python", 5.869);
		langScores.put("C#", 5.067);
		langScores.put("PHP", 4.010);
		langScores.put("JavaScript", 3.916);
		languageScoresInConfigFile = Collections.unmodifiableMap(langScores);
	}
	
	@Test
	void test_customDelimiterForList()
	{
		BoundPropFunc_customListDelimiter langList = this.getSingleConfigInstance(BoundPropFunc_customListDelimiter.class);
		Assertions.assertEquals(languagesInConfigFile, langList.programmingLanguages());
	}
	
	@Test
	void test_customDelimiterForMap()
	{
		BoundPropFunc_customKVDelimiter langMap = this.getSingleConfigInstance(BoundPropFunc_customKVDelimiter.class);
		Assertions.assertEquals(languageScoresInConfigFile, langMap.programmingLanguageScores());
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
interface BoundPropFunc_dontIgnoreWhitespaces
{
	@BoundProperty(name = "programming.languages", delimiterForList = ",,", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customListDelimiter
{
	@BoundProperty(name = "programming.languages", customTypeOrDeserializer = String.class, delimiterForList = ",,")
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_customKVDelimiter
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
}
