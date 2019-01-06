package com.configlinker.tests;


import com.configlinker.FactorySettingsBuilder;
import com.configlinker.IPropertyValidator;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.enums.Whitespaces;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyNotFoundException;
import com.configlinker.exceptions.PropertyValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
	private static List<String> emailsFromConfig;
	private static List<Email> emailObjectsFromConfig;
	
	
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
		emailsFromConfig = Collections.unmodifiableList(emails);
		
		ArrayList<Email> emailObjects = new ArrayList<>();
		emailObjects.add(new Email("vitaliy.mayko@physics.ua"));
		emailObjects.add(new Email("zinovij.nazarchuk@physics.ua"));
		emailObjects.add(new Email("mark.gabovich@physics.ua"));
		emailObjectsFromConfig = Collections.unmodifiableList(emailObjects);
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
		
		Assertions
			.assertEquals("Property value 'Association of Physicists of %Ukraine%' doesn't match pattern '[\\w\\d \"'().]{3,150}'.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_regexValidationList()
	{
		RegexValidatorList emails = this.getSingleConfigInstance(RegexValidatorList.class);
		Assertions.assertEquals(emailsFromConfig, emails.emailsList());
	}
	
	@Test
	void test_regexValidationList_error()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			RegexValidatorList_withError emails = this.getSingleConfigInstance(RegexValidatorList_withError.class);
		});
		
		Assertions.assertEquals(
			"Property value 'vitaliy..mayko@physics.ua' doesn't match pattern '[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?'.",
			exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_regexValidationMap()
	{
		// workgroup.statusMap = a:active, s:suspended, c:closed, d:destroyed
		LinkedHashMap<String, String> statusesMapFromConfig = new LinkedHashMap<>();
		statusesMapFromConfig.put("a", "active");
		statusesMapFromConfig.put("s", "suspended");
		statusesMapFromConfig.put("c", "closed");
		statusesMapFromConfig.put("d", "destroyed");
		
		RegexValidatorMap statuses = this.getSingleConfigInstance(RegexValidatorMap.class);
		Assertions.assertEquals(statusesMapFromConfig, statuses.statusMap());
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
	void test_customValidatorArray()
	{
		CustomValidatorArray emails = this.getSingleConfigInstance(CustomValidatorArray.class);
		Assertions.assertArrayEquals(emailsFromConfig.toArray(new String[emailsFromConfig.size()]), emails.emailsArray());
	}
	
	@Test
	void test_customValidatorArray_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorArray_withError emails = this.getSingleConfigInstance(CustomValidatorArray_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorList()
	{
		CustomValidatorList emails = this.getSingleConfigInstance(CustomValidatorList.class);
		Assertions.assertEquals(emailsFromConfig, emails.emailsList());
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
		LinkedHashSet<String> emailsSetInConfig = new LinkedHashSet<>(emailsFromConfig);
		
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
		LinkedHashMap<String, String> emailsMapFromConfig = new LinkedHashMap<>();
		emailsMapFromConfig.put("vitaliy", "vitaliy.mayko@physics.ua");
		emailsMapFromConfig.put("zinovij", "zinovij.nazarchuk@physics.ua");
		emailsMapFromConfig.put("mark", "mark.gabovich@physics.ua");
		
		CustomValidatorMap emails = this.getSingleConfigInstance(CustomValidatorMap.class);
		Assertions.assertEquals(emailsMapFromConfig, emails.emailsMap());
	}
	
	@Test
	void test_customValidatorMap_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorMap_withError emails = this.getSingleConfigInstance(CustomValidatorMap_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' for key 'zinovij' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// tests for validate custom object values: object, arrayOfObjects listOfObjects, setOfObjects, mapOfObjects
	// --------------------------------------------------------------------------------
	
	@Test
	void test_customValidatorObj()
	{
		CustomValidatorObj email = this.getSingleConfigInstance(CustomValidatorObj.class);
		Assertions.assertEquals(new Email("mark.gabovich@physics.ua"), email.email());
	}
	
	@Test
	void test_customValidatorObj_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorObj_withError email = this.getSingleConfigInstance(CustomValidatorObj_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorArrayObj()
	{
		CustomValidatorArrayObj emails = this.getSingleConfigInstance(CustomValidatorArrayObj.class);
		Assertions.assertArrayEquals(emailObjectsFromConfig.toArray(new Email[emailObjectsFromConfig.size()]), emails.emailsArray());
	}
	
	@Test
	void test_customValidatorArrayObj_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorArrayObj_withError emails = this.getSingleConfigInstance(CustomValidatorArrayObj_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorListObj()
	{
		CustomValidatorListObj emails = this.getSingleConfigInstance(CustomValidatorListObj.class);
		Assertions.assertEquals(emailObjectsFromConfig, emails.emailsList());
	}
	
	@Test
	void test_customValidatorListObj_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorListObj_withError emails = this.getSingleConfigInstance(CustomValidatorListObj_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorSetObj()
	{
		LinkedHashSet<Email> emailsSetInConfig = new LinkedHashSet<>(emailObjectsFromConfig);
		
		CustomValidatorSetObj emails = this.getSingleConfigInstance(CustomValidatorSetObj.class);
		Assertions.assertEquals(emailsSetInConfig, emails.emailsSet());
	}
	
	@Test
	void test_customValidatorSetObj_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorSetObj_withError emails = this.getSingleConfigInstance(CustomValidatorSetObj_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	@Test
	void test_customValidatorMapObj()
	{
		LinkedHashMap<String, Email> emailsMapFromConfig = new LinkedHashMap<>();
		emailsMapFromConfig.put("vitaliy", new Email("vitaliy.mayko@physics.ua"));
		emailsMapFromConfig.put("zinovij", new Email("zinovij.nazarchuk@physics.ua"));
		emailsMapFromConfig.put("mark", new Email("mark.gabovich@physics.ua"));
		
		CustomValidatorMapObj emails = this.getSingleConfigInstance(CustomValidatorMapObj.class);
		Assertions.assertEquals(emailsMapFromConfig, emails.emailsMap());
	}
	
	@Test
	void test_customValidatorMapObj_error()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			CustomValidatorMapObj_withError emails = this.getSingleConfigInstance(CustomValidatorMapObj_withError.class);
		});
		
		Assertions.assertEquals("'zinovij#nazarchuk@physic.ua' for key 'zinovij' not in 'physics.ua' domain.", exception.getMessage());
		Assertions.assertNull(exception.getCause());
	}
	
	// --------------------------------------------------------------------------------
	
	@Test
	void test_errorBehaviorOverrideIn_BoundProperty()
	{
		ErrorBehaviorOverrideInProperty errorOverrideInProperty = getSingleConfigInstance(ErrorBehaviorOverrideInProperty.class);
		Assertions.assertNull(errorOverrideInProperty.nullValueReturnNull());
		Assertions.assertNull(errorOverrideInProperty.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehaviorOverrideIn_BoundObject()
	{
		ErrorBehaviorOverrideInObject errorOverrideInObject = getSingleConfigInstance(ErrorBehaviorOverrideInObject.class);
		Assertions.assertNull(errorOverrideInObject.nullValueReturnNull());
		Assertions.assertNull(errorOverrideInObject.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehaviorOverrideIn_BoundPropertyAndObject_1()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehaviorOverrideInPropertyAndObject errorOverrideInPropertyAndObject = getSingleConfigInstance(ErrorBehaviorOverrideInPropertyAndObject.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'com.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject', method 'nullValueThrowException'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorOverrideIn_BoundPropertyAndObject_2()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehaviorOverrideInPropertyAndObject2 errorOverrideInPropertyAndObject2 = getSingleConfigInstance(
				ErrorBehaviorOverrideInPropertyAndObject2.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.empty' not found, config interface 'com.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject2', method 'emptyValueThrowException'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorForNull_Default()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehaviorForNullDefault errorDefault = getSingleConfigInstance(ErrorBehaviorForNullDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'com.configlinker.tests.ErrorBehaviorForNullDefault', method 'nullValueDefaultErrorBehavior'.",
			exception.getMessage());
		
		
		PropertyNotFoundException exception2 = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
			ErrorBehaviorForNullDefault errorDefault2 = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForNullDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'com.configlinker.tests.ErrorBehaviorForNullDefault', method 'nullValueDefaultErrorBehavior'.",
			exception2.getMessage());
	}
	
	@Test
	void test_errorBehaviorForNullOverrideIn_FactorySettings()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.RETURN_NULL);
		ErrorBehaviorForNullDefault errorDefault = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForNullDefault.class);
		Assertions.assertNull(errorDefault.nullValueDefaultErrorBehavior());
	}
	
	@Test
	void test_errorBehaviorForEmpty_Default()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehaviorForEmptyDefault errorDefault = getSingleConfigInstance(ErrorBehaviorForEmptyDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.empty' not found, config interface 'com.configlinker.tests.ErrorBehaviorForEmptyDefault', method 'emptyValueDefaultErrorBehavior'.",
			exception.getMessage());
		
		
		PropertyNotFoundException exception2 = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
			ErrorBehaviorForEmptyDefault errorDefault2 = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForEmptyDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.empty' not found, config interface 'com.configlinker.tests.ErrorBehaviorForEmptyDefault', method 'emptyValueDefaultErrorBehavior'.",
			exception2.getMessage());
	}
	
	@Test
	void test_errorBehaviorForEmptyOverrideIn_FactorySettings()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.RETURN_NULL);
		ErrorBehaviorForEmptyDefault errorDefault = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForEmptyDefault.class);
		Assertions.assertNull(errorDefault.emptyValueDefaultErrorBehavior());
	}
	
	@Test
	void test_errorBehaviorOverrideIn_BoundPropertyAndObjectAndFactorySettings_1()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.RETURN_NULL);
			ErrorBehaviorOverrideInPropertyAndObject3 errorOverrideInPropertyAndObject3 = getSingleConfigInstance(factorySettingsBuilder,
				ErrorBehaviorOverrideInPropertyAndObject3.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'com.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject3', method 'nullValueThrowException'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorOverrideIn_BoundPropertyAndObjectAndFactorySettings_2()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.RETURN_NULL);
			ErrorBehaviorOverrideInPropertyAndObject4 errorOverrideInPropertyAndObject4 = getSingleConfigInstance(factorySettingsBuilder,
				ErrorBehaviorOverrideInPropertyAndObject4.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.empty' not found, config interface 'com.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject4', method 'emptyValueThrowException'.",
			exception.getMessage());
	}
	
	// TODO: check throwing error on property parse (PropertyMatchException) and property map (PropertyMapException), when behaviour == NULL
	
	
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter
{
	@BoundProperty(name = "programming.languages", delimList = ",,")
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomListDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages", delimList = ",,", whitespaces = Whitespaces.ACCEPT)
	List<String> programmingLanguages();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter
{
	@BoundProperty(name = "programming.languages.popularity.2017", delimList = ";", delimKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_acceptWhitespaces
{
	@BoundProperty(name = "programming.languages.popularity.2017", delimList = ";", delimKeyValue = "@", whitespaces = Whitespaces.ACCEPT)
	Map<String, Double> programmingLanguageScores();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomKeyValueDelimiter_whitespaceOverride
{
	@BoundProperty(name = "programming.languages.popularity.2017", delimList = ";", delimKeyValue = "@")
	Map<String, Double> programmingLanguageScores();
	
	@BoundProperty(name = "programming.languages.popularity.2017", delimList = ";", delimKeyValue = "@", whitespaces = Whitespaces.ACCEPT)
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
	
	@BoundProperty(name = "workgroup.emails", regex = emailPattern)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", regex = RegexValidatorList.emailPattern)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorMap
{
	String statusPattern = "^(active|suspended|closed|destroyed)$";
	
	@BoundProperty(name = "workgroup.statuses", regex = statusPattern)
	Map<String, String> statusMap();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface RegexValidatorMap_withError
{
	@BoundProperty(name = "workgroup.statuses.error", regex = RegexValidatorMap.statusPattern)
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

class EmailMapDomainValidator implements IPropertyValidator<Object[]>
{
	@Override
	public void validate(Object[] value) throws PropertyValidateException
	{
		// value[0] -- key, and is always String
		// value[1] -- value, could be any object depending from return type in interface method
		
		if (!((String) value[1]).endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value[1] + "' for key '" + value[0] + "' not in 'physics.ua' domain.");
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
	@BoundProperty(name = "workgroup.emails", validator = EmailDomainValidator.class)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorList_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailDomainValidator.class)
	List<String> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSet
{
	@BoundProperty(name = "workgroup.emails", validator = EmailDomainValidator.class)
	Set<String> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSet_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailDomainValidator.class)
	Set<String> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMap
{
	@BoundProperty(name = "workgroup.emails.map", validator = EmailMapDomainValidator.class)
	Map<String, String> emailsMap();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMap_withError
{
	@BoundProperty(name = "workgroup.emails.map.error", validator = EmailMapDomainValidator.class)
	Map<String, String> emailsMap();
}

// --------------------------------------------------------------------------------

class Email
{
	private final String email;
	
	Email(String email)
	{
		this.email = email;
	}
	
	String getEmail()
	{
		return email;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Email)) return false;
		
		Email email1 = (Email) o;
		
		return getEmail().equals(email1.getEmail());
	}
	
	@Override
	public int hashCode()
	{
		return getEmail().hashCode();
	}
}

class EmailObjDomainValidator implements IPropertyValidator<Email>
{
	@Override
	public void validate(Email value) throws PropertyValidateException
	{
		if (!value.getEmail().endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value.getEmail() + "' not in 'physics.ua' domain.");
	}
}

class EmailObjMapDomainValidator implements IPropertyValidator<Object[]>
{
	@Override
	public void validate(Object[] value) throws PropertyValidateException
	{
		// value[0] -- key, and is always String
		// value[1] -- value, could be any object depending from return type in interface method
		
		Email emailObj = (Email) value[1];
		if (!(emailObj.getEmail().endsWith("@physics.ua")))
			throw new PropertyValidateException("'" + emailObj.getEmail() + "' for key '" + value[0] + "' not in 'physics.ua' domain.");
	}
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorObj
{
	@BoundProperty(name = "workgroup.email", validator = EmailObjDomainValidator.class)
	Email email();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorObj_withError
{
	@BoundProperty(name = "workgroup.email.error", validator = EmailObjDomainValidator.class)
	Email email();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorArrayObj
{
	@BoundProperty(name = "workgroup.emails", validator = EmailObjDomainValidator.class)
	Email[] emailsArray();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorArrayObj_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailObjDomainValidator.class)
	Email[] emailsArray();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorListObj
{
	@BoundProperty(name = "workgroup.emails", validator = EmailObjDomainValidator.class)
	List<Email> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorListObj_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailObjDomainValidator.class)
	List<Email> emailsList();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSetObj
{
	@BoundProperty(name = "workgroup.emails", validator = EmailObjDomainValidator.class)
	Set<Email> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorSetObj_withError
{
	@BoundProperty(name = "workgroup.emails.error", validator = EmailObjDomainValidator.class)
	Set<Email> emailsSet();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMapObj
{
	@BoundProperty(name = "workgroup.emails.map", validator = EmailObjMapDomainValidator.class)
	Map<String, Email> emailsMap();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface CustomValidatorMapObj_withError
{
	@BoundProperty(name = "workgroup.emails.map.error", validator = EmailObjMapDomainValidator.class)
	Map<String, Email> emailsMap();
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface ErrorBehaviorForNullDefault
{
	@BoundProperty(name = "workgroup.null")
	String nullValueDefaultErrorBehavior();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface ErrorBehaviorForEmptyDefault
{
	@BoundProperty(name = "workgroup.empty")
	String emptyValueDefaultErrorBehavior();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface ErrorBehaviorOverrideInProperty
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.RETURN_NULL)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty", errorBehavior = ErrorBehavior.RETURN_NULL)
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL)
interface ErrorBehaviorOverrideInObject
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL)
interface ErrorBehaviorOverrideInPropertyAndObject
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.THROW_EXCEPTION)
	String nullValueThrowException();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL)
interface ErrorBehaviorOverrideInPropertyAndObject2
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty", errorBehavior = ErrorBehavior.THROW_EXCEPTION)
	String emptyValueThrowException();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.THROW_EXCEPTION)
interface ErrorBehaviorOverrideInPropertyAndObject3
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.RETURN_NULL)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.null")
	String nullValueThrowException();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.THROW_EXCEPTION)
interface ErrorBehaviorOverrideInPropertyAndObject4
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.RETURN_NULL)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueThrowException();
}
