package com.configlinker.tests;


import com.configlinker.IPropertyValidator;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.Whitespaces;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundPropertyTest extends AbstractBaseTest
{
	private static List<String> languagesInConfigFile;
	private static List<String> languagesInConfigFile_withSpaces;
	private static Map<String, Double> languageScoresInConfigFile;
	private static Map<String, Double> languageScoresInConfigFile_withSpaces;
	private static List<String> emailsInConfig;
	
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
		
		ArrayList<String> emails = new ArrayList<>();
		emails.add("vitaliy.mayko@physics.ua");
		emails.add("zinovij.nazarchuk@physics.ua");
		emails.add("mark.gabovich@physics.ua");
		emailsInConfig = Collections.unmodifiableList(emails);
	}
	
	// --------------------------------------------------------------------------------
	
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
		
		Assertions.assertEquals("Property value 'Association of Physicists of %Ukraine%' doesn't match pattern '[\\w\\d \"'().]{3,150}'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_regexValidationList()
	{
		ArrayList<String> emailsFromConfig = new ArrayList<>();
		emailsFromConfig.add("vitaliy.mayko@physics.ua");
		emailsFromConfig.add("zinovij.nazarchuk@physics.ua");
		emailsFromConfig.add("mark.gabovich@physics.ua");
		
		RegexValidatorList emails = this.getSingleConfigInstance(RegexValidatorList.class);
		Assertions.assertEquals(emailsFromConfig, emails.emailsList());
	}
	
	@Test
	void test_regexValidationList_error()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			RegexValidatorList_withError emails = this.getSingleConfigInstance(RegexValidatorList_withError.class);
		});
		
		Assertions.assertEquals("Property value 'vitaliy..mayko@physics.ua' doesn't match pattern '[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_regexValidationMap()
	{
		// workgroup.statusMap = a:active, s:suspended, c:closed, d:destroyed
		LinkedHashMap<String, String> statusesFromConfig = new LinkedHashMap<>();
		statusesFromConfig.put("a", "active");
		statusesFromConfig.put("s", "suspended");
		statusesFromConfig.put("c", "closed");
		statusesFromConfig.put("d", "destroyed");
		
		RegexValidatorMap statuses = this.getSingleConfigInstance(RegexValidatorMap.class);
		Assertions.assertEquals(statusesFromConfig, statuses.statusMap());
	}
	
	@Test
	void test_regexValidationMap_error()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			RegexValidatorMap_withError statuses = this.getSingleConfigInstance(RegexValidatorMap_withError.class);
		});
		
		Assertions.assertEquals("Property value 'suspend' for key 's' doesn't match pattern '^(active|suspended|closed|destroyed)$'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// --------------------------------------------------------------------------------
	
	@Test
	void test_customValidator()
	{
		CustomValidator email = this.getSingleConfigInstance(CustomValidator.class);
		Assertions.assertEquals("mark.gabovich@physics.ua", email.email());
	}
	
	@Test
	void test_customValidator_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidator_withError email = this.getSingleConfigInstance(CustomValidator_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorList()
	{
		CustomValidatorList emails = this.getSingleConfigInstance(CustomValidatorList.class);
		Assertions.assertEquals(emailsInConfig, emails.emailsList());
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
	
	@Test
	void test_customValidatorSet()
	{
		LinkedHashSet<String> emailsSetInConfig = new LinkedHashSet<>(emailsInConfig);
		
		CustomValidatorSet emails = this.getSingleConfigInstance(CustomValidatorSet.class);
		Assertions.assertEquals(emailsSetInConfig, emails.emailsSet());
	}
	
	@Test
	void test_customValidatorSet_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorSet_withError emails = this.getSingleConfigInstance(CustomValidatorSet_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorMap()
	{
		LinkedHashMap<String,String> emailsInFromConfig = new LinkedHashMap<>();
		emailsInFromConfig.put("vitaliy", "vitaliy.mayko@physics.ua");
		emailsInFromConfig.put("zinovij", "zinovij.nazarchuk@physics.ua");
		emailsInFromConfig.put("mark", "mark.gabovich@physics.ua");
		
		CustomValidatorMap emails = this.getSingleConfigInstance(CustomValidatorMap.class);
		Assertions.assertEquals(emailsInFromConfig, emails.emailsMap());
	}
	
	@Test
	void test_customValidatorMap_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorSet_withError emails = this.getSingleConfigInstance(CustomValidatorSet_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// TODO: tests for values: object, listOfObjects, setOfObjects, mapOfObjects
	
	// --------------------------------------------------------------------------------
	
	@Test @Disabled("TODO: implement")
	void test_errorBehaviour()
	{
		//TODO: implement
		Assertions.fail("Not implemented.");
	}

}


@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter
{
	@BoundProperty(name = "programming.languages", customType = String.class, delimList = ",,")
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages", customType = String.class, delimList = ",,", whitespaces = Whitespaces.ACCEPT)
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter
{
	@BoundProperty(name = "programming.languages.popularity.2017", customType = Double.class, delimList = ";", delimKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages.popularity.2017", customType = Double.class, delimList = ";", delimKeyValue = "@", whitespaces = Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_whitespaceOverride
{
	@BoundProperty(name = "programming.languages.popularity.2017", customType = Double.class, delimList = ";", delimKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
	
	@BoundProperty(name = "programming.languages.popularity.2017", customType = Double.class, delimList = ";", delimKeyValue = "@", whitespaces = Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores_acceptWhitespaces();
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidator
{
	@BoundProperty(name = "workgroup.name", regex = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidator_withError
{
	@BoundProperty(name = "workgroup.name.error", regex = "[\\w\\d \"'().]{3,150}")
	String workgroupName();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorList
{
	String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	@BoundProperty(name = "workgroup.emails", customType = String.class, regex = emailPattern)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", customType = String.class, regex = RegexValidatorList.emailPattern)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorMap
{
	String statusPattern = "^(active|suspended|closed|destroyed)$";
	
	@BoundProperty(name = "workgroup.statuses", customType = String.class, regex = statusPattern)
	Map<String, String> statusMap();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorMap_withError
{
	@BoundProperty(name = "workgroup.statuses.error", customType = String.class, regex = RegexValidatorMap.statusPattern)
	Map<String, String> statusMap();
}

// --------------------------------------------------------------------------------

class EmailDomainValidator implements IPropertyValidator<String>
{
	@Override
	public void validate(String value) throws PropertyValidateException
	{
		if (!value.endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value + "' not in 'physics.ua' domain.");
	}
}

class EmailDomainMapValidator implements IPropertyValidator<Object[]>
{
	@Override
	public void validate(Object[] value) throws PropertyValidateException
	{
		// value[0] -- key, and is always String
		// value[1] -- value, could be any object depending from return type in interface method
		
		if (!((String) value[1]).endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value[1] + "' for key " + value[0] + " not in 'physics.ua' domain.");
	}
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidator
{
	@BoundProperty(name = "workgroup.email", validator = EmailDomainValidator.class)
	String email();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidator_withError
{
	@BoundProperty(name = "workgroup.email.error", validator = EmailDomainValidator.class)
	String email();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorArray
{
	@BoundProperty(name = "workgroup.emails", validator = EmailDomainValidator.class)
	String[] emailsArray();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorArray_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailDomainValidator.class)
	String[] emailsArray();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorList
{
	@BoundProperty(name = "workgroup.emails", customType = String.class, validator = EmailDomainValidator.class)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", customType = String.class, validator = EmailDomainValidator.class)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSet
{
	@BoundProperty(name = "workgroup.emails", customType = String.class, validator = EmailDomainValidator.class)
	Set<String> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSet_withError
{
	@BoundProperty(name = "workgroup.emails.error", customType = String.class, validator = EmailDomainValidator.class)
	Set<String> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMap
{
	@BoundProperty(name = "workgroup.emails.map", customType = String.class, validator = EmailDomainMapValidator.class)
	Map<String, String> emailsMap();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMap_withError
{
	@BoundProperty(name = "workgroup.emails.map.error", customType = String.class, validator = EmailDomainMapValidator.class)
	Map<String, String> emailsMap();
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface ErrorBehavior
{

}

