package com.configlinker.tests;


import com.configlinker.IPropertyValidator;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;
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
		CustomListDelimiter langList = this.getSingleConfigInstance(CustomListDelimiter.class);
		Assertions.assertEquals(BoundPropertyTest.languagesInConfigFile, langList.programmingLanguages());
	}
	
	@Test
	void test_customDelimiterForListAndAcceptWhitespaces()
	{
		CustomListDelimiter_acceptWhitespaces langList = this.getSingleConfigInstance(CustomListDelimiter_acceptWhitespaces.class);
		Assertions.assertEquals(BoundPropertyTest.languagesInConfigFile_withSpaces, langList.programmingLanguages());
	}
	
	@Test
	void test_customDelimiterForMap()
	{
		CustomKeyValueDelimiter langMap = this.getSingleConfigInstance(CustomKeyValueDelimiter.class);
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile, langMap.programmingLanguageScores());
	}
	
	@Test
	void test_customDelimiterForMapAndAcceptWhitespaces()
	{
		CustomKeyValueDelimiter_acceptWhitespaces langMap = this.getSingleConfigInstance(CustomKeyValueDelimiter_acceptWhitespaces.class);
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile_withSpaces, langMap.programmingLanguageScores());
	}
	
	@Test
	void test_whitespaceOverride()
	{
		CustomKeyValueDelimiter_whitespaceOverride langMap = this
		  .getSingleConfigInstance(CustomKeyValueDelimiter_whitespaceOverride.class);
		
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile, langMap.programmingLanguageScores());
		Assertions.assertEquals(BoundPropertyTest.languageScoresInConfigFile_withSpaces, langMap.programmingLanguageScores_acceptWhitespaces());
	}
	
	// --------------------------------------------------------------------------------
	
	@Test
	void test_regexValidation()
	{
		RegexValidator workgroup = this.getSingleConfigInstance(RegexValidator.class);
		Assertions.assertEquals("Association of Physicists of Ukraine", workgroup.workgroupName());
	}
	
	@Test
	void test_regexValidation_error()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			RegexValidator_withError workgroup = this.getSingleConfigInstance(RegexValidator_withError.class);
		});
		
		Assertions.assertEquals("Property 'Association of Physicists of %Ukraine%' doesn't match pattern '[\\w\\d \"'().]{3,150}'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_regexValidationList()
	{
		ArrayList<String> emailsFromConfig = new ArrayList<>();
		emailsFromConfig.add("vitaliy.mayko@physics.ua");
		emailsFromConfig.add("zinovij.nazarchuk@physics.ua");
		emailsFromConfig.add("mark.gabovich@physics.ua");
		
		BoundPropFunc_regexValidatorList emails = this.getSingleConfigInstance(BoundPropFunc_regexValidatorList.class);
		Assertions.assertEquals(emailsFromConfig, emails.emailList());
	}
	
	@Test
	void test_regexValidationList_error()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			RegexValidatorList_withError emails = this.getSingleConfigInstance(RegexValidatorList_withError.class);
		});
		
		Assertions.assertEquals("Property 'vitaliy..mayko@physics.ua' doesn't match pattern '[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// --------------------------------------------------------------------------------
	
	@Test
	void test_customValidatorList()
	{
		ArrayList<String> emailsFromConfig = new ArrayList<>();
		emailsFromConfig.add("vitaliy.mayko@physics.ua");
		emailsFromConfig.add("zinovij.nazarchuk@physics.ua");
		emailsFromConfig.add("mark.gabovich@physics.ua");
		
		CustomValidatorList emails = this.getSingleConfigInstance(CustomValidatorList.class);
		Assertions.assertEquals(emailsFromConfig, emails.emailList());
	}
	
	@Test
	void test_customValidatorList_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorList_withError emails = this.getSingleConfigInstance(CustomValidatorList_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// --------------------------------------------------------------------------------
	
	// TODO: tests fro regex and validte for array, list, set, map (for strings and any other type)
	
	@Test @Disabled("TODO: implement")
	void test_errorBehaviour()
	{
		//TODO: implement
	}

}


@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter
{
	@BoundProperty(name = "programming.languages", customTypeOrDeserializer = String.class, delimiterForList = ",,")
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages", customTypeOrDeserializer = String.class, delimiterForList = ",,", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_whitespaceOverride
{
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
	
	@BoundProperty(name = "programming.languages.popularity.2017", customTypeOrDeserializer = Double.class, delimiterForList = ";", delimiterForKeyValue = "@", whitespaces = BoundProperty.Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores_acceptWhitespaces();
}


@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidator
{
	@BoundProperty(name = "workgroup.name", regexPattern = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidator_withError
{
	@BoundProperty(name = "workgroup.name.error", regexPattern = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface BoundPropFunc_regexValidatorList
{
	String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	@BoundProperty(name = "workgroup.emails", customTypeOrDeserializer = String.class, regexPattern = emailPattern)
	List<String> emailList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", customTypeOrDeserializer = String.class, regexPattern = BoundPropFunc_regexValidatorList.emailPattern)
	List<String> emailList();
}


class EmailDomainValidator implements IPropertyValidator<String>
{
	@Override
	public void validate(String value) throws PropertyValidateException
	{
		if (!value.endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value + "' not in 'physics.ua' domain.");
	}
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorList
{
	@BoundProperty(name = "workgroup.emails", customTypeOrDeserializer = String.class, validator = EmailDomainValidator.class)
	List<String> emailList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", customTypeOrDeserializer = String.class, validator = EmailDomainValidator.class)
	List<String> emailList();
}


@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface ErrorBehavior
{

}

