package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.parsers.ParserFactory;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public final class MapperFactory
{
	private MapperFactory()
	{
	}
	
	@SuppressWarnings("unchecked")
	public static PropertyMapper create(BoundProperty boundPropertyAnnotation, Method propertyMethod, boolean ignoreWhitespaces) throws PropertyMapException
	{
		
		Class<?> returnType = propertyMethod.getReturnType();
		
		String strRegexpPattern = boundPropertyAnnotation.regexPattern();
		Pattern regexpPattern = null;
		if (strRegexpPattern.length() > 0)
			try
			{
				regexpPattern = Pattern.compile(strRegexpPattern);
			}
			catch (PatternSyntaxException e)
			{
				throw new PropertyMapException(
				  "Cannot compile regexp pattern for '" + propertyMethod.getDeclaringClass().getName() + "::" + propertyMethod.getName() + "'.", e);
			}
		
		Class<? extends PropertyValidator> validator_class = boundPropertyAnnotation.validator();
		PropertyValidator validator = null;
		if (validator_class != PropertyValidator.class)
			try
			{
				validator = validator_class.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				throw new PropertyMapException(
				  "Cannot create validator for '" + propertyMethod.getDeclaringClass().getName() + "::" + propertyMethod.getName() + "'.", e).logAndReturn();
			}
		
		Class<?> customTypeOrDeserializer = boundPropertyAnnotation.customTypeOrDeserializer();
		BoundProperty.DeserializationMethod deserializationMethod = boundPropertyAnnotation.deserializationMethod();
		
		String delimiterForList = boundPropertyAnnotation.delimiterForList();
		String delimiterForKeyValue = boundPropertyAnnotation.delimiterForKeyValue();
		
		// --------------------------------------------------------------------------------
		
		if (returnType.isPrimitive())
		{
			customTypeOrDeserializer = ParserFactory.getWrapperForPrimitive(returnType);
			deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		}
		
		if (ParserFactory.isPrimitiveWrapper(returnType) || returnType == String.class || returnType.isEnum())
		{
			customTypeOrDeserializer = returnType;
			deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		}
		
		if (returnType.isArray())
		{
			Class<?> arrayType = returnType.getComponentType();
			if (arrayType.isPrimitive())
				arrayType = ParserFactory.getWrapperForPrimitive(arrayType);
			
			if (ParserFactory.isPrimitiveWrapper(arrayType) || arrayType == String.class || arrayType.isEnum())
			{
				customTypeOrDeserializer = arrayType;
				deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
				
				if (customTypeOrDeserializer == Character.class)
					regexpPattern = Pattern.compile(".");
			}
			
			if (customTypeOrDeserializer == Object.class)
				customTypeOrDeserializer = arrayType;
		}
		
		if (customTypeOrDeserializer.isPrimitive())
			throw new PropertyMapException(
			  "Value of '@BoundProperty.customTypeOrDeserializer' can not be a primitive type class, but current value is '" + customTypeOrDeserializer
				.getName() + "' and current return type is '" + returnType.getName() + "'.").logAndReturn();
		
		if (customTypeOrDeserializer == Object.class && (List.class.isAssignableFrom(returnType) || Set.class.isAssignableFrom(returnType) || Map.class
		  .isAssignableFrom(returnType)))
			throw new PropertyMapException("For type '" + returnType
			  .getName() + "' you must specify its generic type in '@BoundProperty.customTypeOrDeserializer' and choose '@BoundProperty.deserializationMethod'; method leading to error: '" + propertyMethod
			  .getDeclaringClass().getName() + "." + propertyMethod.getName() + "()'.").logAndReturn();
		
		if (customTypeOrDeserializer == Object.class)
			customTypeOrDeserializer = returnType;
		
		// --------------------------------------------------------------------------------
		
		Executable executable;
		
		executable = getMethodForPredefinedType(customTypeOrDeserializer);
		if (executable != null)
			deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING;
		else
		{
			if (deserializationMethod == BoundProperty.DeserializationMethod.AUTO)
				deserializationMethod = determineDeserializationMethod(customTypeOrDeserializer);
			executable = getMethodForType(customTypeOrDeserializer, deserializationMethod);
		}
		
		// TODO: Additional check for 'customTypeOrDeserializer', if return type is List, Set or Map and the Generic type is one of the List, Set or Map too.
		
		// --------------------------------------------------------------------------------
		
		PropertyParser propertyParser = ParserFactory.create(returnType, deserializationMethod);

		if (returnType == String.class)
			return new StringStubPropertyMapper(propertyParser, ignoreWhitespaces, executable, regexpPattern);
		
		if (returnType.isArray())
		{
			if (returnType.getComponentType().isPrimitive())
				return new ArrayPrimitiveMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
			else
				return new ArrayMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		}
		
		if (List.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new ListStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, delimiterForList);
			else
				return new ListObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		
		if (Set.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new SetStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, delimiterForList);
			else
				return new SetObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		
		if (Map.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new MapStringStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, delimiterForList,
				  delimiterForKeyValue);
			else
				return new MapStringObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList,
				  delimiterForKeyValue);
		
		return new CustomObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList,
		  delimiterForKeyValue);
	}
	
	private static BoundProperty.DeserializationMethod determineDeserializationMethod(Class<?> customTypeOrDeserializer) throws PropertyMapException
	{
		BoundProperty.DeserializationMethod result = null;
		HashSet<Executable> methods = new HashSet<>();
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredConstructor(String.class));
			result = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredConstructor(Map.class));
			result = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("valueOf", String.class));
			result = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("valueOf", Map.class));
			result = BoundProperty.DeserializationMethod.VALUEOF_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("deserialize", String.class));
			result = BoundProperty.DeserializationMethod.DESERIALIZER_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("deserialize", Map.class));
			result = BoundProperty.DeserializationMethod.DESERIALIZER_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		methods.remove(null);
		if (methods.size() > 1)
			throw new PropertyMapException("'" + customTypeOrDeserializer
			  .getName() + "' contains more than one deserialization implementation, while @BoundProperty.deserializationMethod() set to AUTO. See  class 'BoundProperty.DeserializationMethod' for more details.")
			  .logAndReturn();
		
		return result;
	}
	
	private static Executable getMethodForType(Class<?> customTypeOrDeserializer, BoundProperty.DeserializationMethod deserializationMethod) throws
	  PropertyMapException
	{
		try
		{
			Executable executable;
			// This have done in order to remove the implementation details out of the public api
			switch (deserializationMethod)
			{
				case CONSTRUCTOR_STRING:
					executable = customTypeOrDeserializer.getDeclaredConstructor(String.class);
					break;
				case CONSTRUCTOR_MAP:
					executable = customTypeOrDeserializer.getDeclaredConstructor(Map.class);
					break;
				case VALUEOF_STRING:
					executable = customTypeOrDeserializer.getDeclaredMethod("valueOf", String.class);
					break;
				case VALUEOF_MAP:
					executable = customTypeOrDeserializer.getDeclaredMethod("valueOf", Map.class);
					break;
				case DESERIALIZER_STRING:
					executable = customTypeOrDeserializer.getDeclaredMethod("deserialize", String.class);
					break;
				case DESERIALIZER_MAP:
					executable = customTypeOrDeserializer.getDeclaredMethod("deserialize", Map.class);
					break;
				default:
					throw new PropertyMapException("There is no suitable version for @BoundProperty.DeserializationMethod='" + deserializationMethod
					  .name() + "'; custom type or deserializer: '" + customTypeOrDeserializer.getName() + "'.").logAndReturn();
			}
			return executable;
		}
		catch (NoSuchMethodException e)
		{
			throw new PropertyMapException("Can not find deserialization method in: '" + customTypeOrDeserializer
			  .getName() + "'; @BoundProperty.DeserializationMethod='" + deserializationMethod.name() + "'.", e).logAndReturn();
		}
	}
	
	// maybe it will be needed to change AbstractPropertyMapper.createObject() method.
	private static Executable getMethodForPredefinedType(Class<?> customTypeOrDeserializer)
	{
		Executable executable = null;
		try
		{
			if (customTypeOrDeserializer == Character.class)
				executable = CharacterMapper.class.getDeclaredMethod("valueOf", String.class);
			
			if (customTypeOrDeserializer == String.class)
				executable = String.class.getDeclaredMethod("valueOf", Object.class);
			
			if (customTypeOrDeserializer == URL.class)
				executable = URL.class.getConstructor(String.class);
			
			if (customTypeOrDeserializer == URI.class)
				executable = URI.class.getConstructor(String.class);
			
			if (InetAddress.class.isAssignableFrom(customTypeOrDeserializer))
				executable = InetAddress.class.getDeclaredMethod("getByName", String.class);
			
			if (customTypeOrDeserializer == UUID.class)
				executable = UUID.class.getMethod("fromString", String.class);
			
			//if (customTypeOrDeserializer == YourType.class)
			// implement for other types
		}
		catch (NoSuchMethodException e)
		{
			throw new PropertyMapException("Can not find deserialization method for predefined type in: '" + customTypeOrDeserializer
			  .getName() + "'.", e).logAndReturn();
		}
		return executable;
	}
}
