package net.crispcode.configlinker.tests;

import net.crispcode.configlinker.FactorySettingsBuilder;
import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.annotations.BoundProperty;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.exceptions.AnnotationAnalyzeException;
import net.crispcode.configlinker.exceptions.PropertyLoadException;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyNotFoundException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BoundPropertyErrorBehaviourTest extends AbstractBaseTest
{
	// test ErrorBehaviour
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
			"Value for property 'workgroup.null' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject#nullValueThrowException'.",
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
			"Value for property 'workgroup.empty' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject2#emptyValueThrowException'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorForNull_Default()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehaviorForNullDefault errorDefault = getSingleConfigInstance(ErrorBehaviorForNullDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorForNullDefault#nullValueDefaultErrorBehavior'.",
			exception.getMessage());
		
		
		PropertyNotFoundException exception2 = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
			ErrorBehaviorForNullDefault errorDefault2 = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForNullDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.null' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorForNullDefault#nullValueDefaultErrorBehavior'.",
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
			"Value for property 'workgroup.empty' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorForEmptyDefault#emptyValueDefaultErrorBehavior'.",
			exception.getMessage());
		
		
		PropertyNotFoundException exception2 = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
			ErrorBehaviorForEmptyDefault errorDefault2 = getSingleConfigInstance(factorySettingsBuilder, ErrorBehaviorForEmptyDefault.class);
		});
		
		Assertions.assertEquals(
			"Value for property 'workgroup.empty' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorForEmptyDefault#emptyValueDefaultErrorBehavior'.",
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
			"Value for property 'workgroup.null' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject3#nullValueThrowException'.",
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
			"Value for property 'workgroup.empty' not found, config interface 'net.crispcode.configlinker.tests.ErrorBehaviorOverrideInPropertyAndObject4#emptyValueThrowException'.",
			exception.getMessage());
	}
	
	// test ErrorBehaviour with defaults
	// --------------------------------------------------------------------------------
	
	@Test
	void test_errorBehavior_WithDefaultsAndException_MissingDefaults()
	{
		AnnotationAnalyzeException exception = Assertions.assertThrows(AnnotationAnalyzeException.class, () -> {
			ErrorBehavior_WithDefaultsAndException_MissingDefaults defaultsAndException_MissingDefaults = getSingleConfigInstance(
				ErrorBehavior_WithDefaultsAndException_MissingDefaults.class);
		});
		
		Assertions.assertEquals(
			"You cannot use ErrorBehavior.TRY_DEFAULTS_OR_NULL or ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION without @BoundObject.defaultSourcePath(), config interface '" + ErrorBehavior_WithDefaultsAndException_MissingDefaults.class
				.getName() + "'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehavior_WithDefaultsAndNull_MissingDefaults()
	{
		AnnotationAnalyzeException exception = Assertions.assertThrows(AnnotationAnalyzeException.class, () -> {
			ErrorBehavior_WithDefaultsAndNull_MissingDefaults defaultsAndNull_MissingDefaults = getSingleConfigInstance(
				ErrorBehavior_WithDefaultsAndNull_MissingDefaults.class);
		});
		
		Assertions.assertEquals(
			"You cannot use ErrorBehavior.TRY_DEFAULTS_OR_NULL or ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION without @BoundObject.defaultSourcePath(), config interface '" + ErrorBehavior_WithDefaultsAndNull_MissingDefaults.class
				.getName() + "'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehavior_WithDefaultsAndNull_DefaultsNotFound()
	{
		PropertyLoadException exception = Assertions.assertThrows(PropertyLoadException.class, () -> {
			ErrorBehavior_WithDefaultsAndNull_DefaultsNotFound defaultsAndNull_MissingDefaults = getSingleConfigInstance(
				ErrorBehavior_WithDefaultsAndNull_DefaultsNotFound.class);
		});
		
		Assertions.assertEquals(
			"Configuration file for defaultSourcePath 'non_existed_file.properties' doesn't exist; see annotation parameter @BoundObject.defaultSourcePath() on interface '" + ErrorBehavior_WithDefaultsAndNull_DefaultsNotFound.class
				.getName() + "'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehavior_WithDefaultsAndException_ReturnDefaults()
	{
		ErrorBehavior_WithDefaultsAndException_ReturnDefaults defaultsAndException_ReturnDefaults = getSingleConfigInstance(ErrorBehavior_WithDefaultsAndException_ReturnDefaults.class);
		Assertions.assertEquals("was_null_value", defaultsAndException_ReturnDefaults.nullValueReturnNull());
		Assertions.assertEquals("was_empty_value", defaultsAndException_ReturnDefaults.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehavior_WithDefaultsAndException_ThrowException()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			ErrorBehavior_WithDefaultsAndException_ThrowException defaultsAndException_ReturnDefaults = getSingleConfigInstance(ErrorBehavior_WithDefaultsAndException_ThrowException.class);
		});
		
		Assertions.assertEquals(
			"Default value for property 'workgroup.empty' not found, config interface '" + ErrorBehavior_WithDefaultsAndException_ThrowException.class.getName() + "#emptyValueReturnNull'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehavior_WithOverrideDefaultsAndException_ReturnDefaults()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
		ErrorBehavior_WithOverrideDefaultsAndException_ReturnDefaults overrideDefaultsAndException_ReturnDefaults = getSingleConfigInstance(
			factorySettingsBuilder, ErrorBehavior_WithOverrideDefaultsAndException_ReturnDefaults.class);
		
		Assertions.assertEquals("was_null_value", overrideDefaultsAndException_ReturnDefaults.nullValueReturnNull());
		Assertions.assertNull(overrideDefaultsAndException_ReturnDefaults.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehavior_WithOverrideDefaultsAndException_ThrowException()
	{
		PropertyNotFoundException exception = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
			FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create().setErrorBehavior(ErrorBehavior.THROW_EXCEPTION);
			ErrorBehavior_WithOverridePropertyDefaultsAndException_ThrowException overrideDefaultsAndException_ThrowException = getSingleConfigInstance(
				factorySettingsBuilder, ErrorBehavior_WithOverridePropertyDefaultsAndException_ThrowException.class);
		});
		
		Assertions.assertEquals(
			"Default value for property 'workgroup.empty' not found, config interface '" + ErrorBehavior_WithOverridePropertyDefaultsAndException_ThrowException.class.getName() + "#emptyValueReturnNull'.",
			exception.getMessage());
	}
	
	@Test
	void test_errorBehavior_WithDefaultsAndNull_WithOverridePropertyNull_ReturnNullAndDefaultsNull()
	{
		ErrorBehavior_WithDefaultsAndNull_WithOverridePropertyNull_ReturnNullAndDefaultsNull _returnNullAndDefaultsNull = getSingleConfigInstance(
			ErrorBehavior_WithDefaultsAndNull_WithOverridePropertyNull_ReturnNullAndDefaultsNull.class);
		
		Assertions.assertNull(_returnNullAndDefaultsNull.nullValueReturnNull());
		Assertions.assertNull(_returnNullAndDefaultsNull.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehavior_WithOverridePropertyDefaultsAndException_ReturnDefaults()
	{
		ErrorBehavior_WithOverridePropertyDefaultsAndException_ReturnDefaults _returnOverrideDefaults = getSingleConfigInstance(
			ErrorBehavior_WithOverridePropertyDefaultsAndException_ReturnDefaults.class);
		
		Assertions.assertEquals("was_null_value", _returnOverrideDefaults.nullValueReturnNull());
		Assertions.assertEquals("was_empty_value", _returnOverrideDefaults.emptyValueReturnNull());
	}
	
	@Test
	void test_errorBehavior_WithOverrideProperDefaultsAndNull_ReturnNullAndDefaults()
	{
		ErrorBehavior_WithOverrideProperDefaultsAndNull_ReturnNullAndDefaults _returnNullAndDefaults = getSingleConfigInstance(
			ErrorBehavior_WithOverrideProperDefaultsAndNull_ReturnNullAndDefaults.class);
		
		Assertions.assertNull(_returnNullAndDefaults.nullValueReturnNull());
		Assertions.assertEquals("was_empty_value", _returnNullAndDefaults.emptyValueReturnNull());
	}
	
	// check throwing error on property parse (PropertyMatchException) and property map (PropertyMapException), when behaviour == NULL
	// --------------------------------------------------------------------------------
	
	@Test
	void test_errorBehaviorReturnNull_onRegexError()
	{
		PropertyMatchException exception = Assertions.assertThrows(PropertyMatchException.class, () -> {
			Regex_withError_returnNullBehaviour errorOnRegex = getSingleConfigInstance(Regex_withError_returnNullBehaviour.class);
		});
		
		Assertions.assertEquals("Property value '22O' doesn't match pattern '^\\d{3}$'.", exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorReturnNull_onValidateError()
	{
		PropertyValidateException exception = Assertions.assertThrows(PropertyValidateException.class, () -> {
			Validator_withError_returnNullBehaviour errorOnValidate = getSingleConfigInstance(Validator_withError_returnNullBehaviour.class);
		});
		
		Assertions.assertEquals("'22O' not equal to '220'.", exception.getMessage());
	}
	
	@Test
	void test_errorBehaviorReturnNull_onMappingError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			PropertyMapException_returnNullBehaviour errorOnMapping = getSingleConfigInstance(PropertyMapException_returnNullBehaviour.class);
		});
	}
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

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
interface ErrorBehavior_WithDefaultsAndException_MissingDefaults
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_NULL)
interface ErrorBehavior_WithDefaultsAndNull_MissingDefaults
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_NULL,
	defaultSourcePath = "non_existed_file.properties")
interface ErrorBehavior_WithDefaultsAndNull_DefaultsNotFound
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ReturnDefaults.properties")
interface ErrorBehavior_WithDefaultsAndException_ReturnDefaults
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ThrowException.properties")
interface ErrorBehavior_WithDefaultsAndException_ThrowException
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ThrowException.properties")
interface ErrorBehavior_WithOverrideDefaultsAndException_ReturnDefaults
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ThrowException.properties")
interface ErrorBehavior_WithOverridePropertyDefaultsAndException_ThrowException
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_NULL,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ThrowException.properties")
interface ErrorBehavior_WithDefaultsAndNull_WithOverridePropertyNull_ReturnNullAndDefaultsNull
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.RETURN_NULL)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty")
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties",
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ReturnDefaults.properties")
interface ErrorBehavior_WithOverridePropertyDefaultsAndException_ReturnDefaults
{
	@BoundProperty(name = "workgroup.null", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
	String emptyValueReturnNull();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties", errorBehavior = ErrorBehavior.RETURN_NULL,
	defaultSourcePath = "ErrorBehavior_WithDefaultsAndException_ReturnDefaults.properties")
interface ErrorBehavior_WithOverrideProperDefaultsAndNull_ReturnNullAndDefaults
{
	@BoundProperty(name = "workgroup.null")
	String nullValueReturnNull();
	
	@BoundProperty(name = "workgroup.empty", errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_NULL)
	String emptyValueReturnNull();
}

// --------------------------------------------------------------------------------

class ValueValidator implements IPropertyValidator<String>
{
	@Override
	public void validate(String value) throws PropertyValidateException
	{
		if (!value.equals("220"))
			throw new PropertyValidateException("'" + value + "' not equal to '220'.");
	}
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface Regex_withError_returnNullBehaviour
{
	@BoundProperty(name = "workgroup.intvalue", regex = "^\\d{3}$", errorBehavior = ErrorBehavior.RETURN_NULL)
	String value();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface Validator_withError_returnNullBehaviour
{
	@BoundProperty(name = "workgroup.intvalue", validator = ValueValidator.class, errorBehavior = ErrorBehavior.RETURN_NULL)
	String value();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface PropertyMapException_returnNullBehaviour
{
	@BoundProperty(name = "workgroup.intvalue", errorBehavior = ErrorBehavior.RETURN_NULL)
	Integer value();
}
