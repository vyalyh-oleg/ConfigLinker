/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.annotations.BoundProperty;
import net.crispcode.configlinker.enums.DeserializationMethod;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.parsers.ParserFactory;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
	public static IPropertyMapper create(BoundProperty boundPropertyAnnotation, Method propertyMethod, boolean ignoreWhitespaces) throws PropertyMapException
	{
		String strRegexpPattern = boundPropertyAnnotation.regex();
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
		
		Class<? extends IPropertyValidator> validator_class = boundPropertyAnnotation.validator();
		IPropertyValidator validator = null;
		if (validator_class != IPropertyValidator.class)
			try
			{
				Constructor<? extends IPropertyValidator> constructor = validator_class.getDeclaredConstructor();
				constructor.setAccessible(true);
				validator = constructor.newInstance();
			}
			catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
			{
				throw new PropertyMapException(
				  "Cannot create validator for '" + propertyMethod.getDeclaringClass().getName() + "::" + propertyMethod.getName() + "'.", e).logAndReturn();
			}
		
		Class<?> customTypeOrDeserializer = boundPropertyAnnotation.customType();
		DeserializationMethod deserializationMethod = boundPropertyAnnotation.deserializationMethod();
		
		String delimiterForList = boundPropertyAnnotation.delimList();
		String delimiterForKeyValue = boundPropertyAnnotation.delimKeyValue();
		
		// --------------------------------------------------------------------------------
		
		Class<?> returnType = propertyMethod.getReturnType();
		
		if (returnType.isPrimitive())
		{
			customTypeOrDeserializer = ParserFactory.getWrapperForPrimitive(returnType);
			deserializationMethod = DeserializationMethod.VALUEOF_STRING;
		}
		
		if (ParserFactory.isPrimitiveWrapper(returnType) || returnType == String.class || returnType.isEnum())
		{
			customTypeOrDeserializer = returnType;
			deserializationMethod = DeserializationMethod.VALUEOF_STRING;
		}
		
		if (returnType.isArray())
		{
			Class<?> arrayType = returnType.getComponentType();
			if (arrayType.isPrimitive())
				arrayType = ParserFactory.getWrapperForPrimitive(arrayType);
			
			if (ParserFactory.isPrimitiveWrapper(arrayType) || arrayType == String.class || arrayType.isEnum())
			{
				customTypeOrDeserializer = arrayType;
				deserializationMethod = DeserializationMethod.VALUEOF_STRING;
				
				if (customTypeOrDeserializer == Character.class)
					regexpPattern = Pattern.compile(".");
			}
			
			if (customTypeOrDeserializer == Object.class)
				customTypeOrDeserializer = arrayType;
		}
		
		if (customTypeOrDeserializer.isPrimitive())
			throw new PropertyMapException(
			  "Value of '@BoundProperty.customType' can not be a primitive type class, but current value is '" + customTypeOrDeserializer
				.getName() + "' and current return type is '" + returnType.getName() + "'.").logAndReturn();
		
		if (customTypeOrDeserializer == Object.class && (List.class.isAssignableFrom(returnType) || Set.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType)))
		{
			Type genericType = propertyMethod.getGenericReturnType();
			if (genericType instanceof ParameterizedType)
			{
				ParameterizedType parameterizedType = ((ParameterizedType) genericType);
				if (List.class.isAssignableFrom(returnType) || Set.class.isAssignableFrom(returnType))
					customTypeOrDeserializer = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				else
					customTypeOrDeserializer = (Class<?>) parameterizedType.getActualTypeArguments()[1];
			}
			
			if (customTypeOrDeserializer == Object.class)
				throw new PropertyMapException("For type '" + returnType.getName() + "' you must specify it's generic type in the angle brackets or in '@BoundProperty.customType'; method leading to error: '" + propertyMethod.getDeclaringClass().getName() + "." + propertyMethod.getName() + "()'.").logAndReturn();
		}
		
		if (customTypeOrDeserializer == Object.class)
			customTypeOrDeserializer = returnType;
		
		// --------------------------------------------------------------------------------
		
		Executable executable;
		
		executable = getMethodForPredefinedType(customTypeOrDeserializer);
		if (executable != null)
			// because currently all predefined types are constructed from strings (this behaviour could be changed with time)
			// TODO: should be added properly logic for 'deserializationMethod' determination
			deserializationMethod = DeserializationMethod.CONSTRUCTOR_STRING;
		else
		{
			if (deserializationMethod == DeserializationMethod.AUTO)
				deserializationMethod = determineDeserializationMethod(customTypeOrDeserializer);
			executable = getMethodForType(customTypeOrDeserializer, deserializationMethod);
		}
		
		// --------------------------------------------------------------------------------
		
		PropertyParser propertyParser = ParserFactory.create(returnType, deserializationMethod);
		
		if (returnType == String.class)
			return new StringStubPropertyMapper(propertyParser, ignoreWhitespaces, executable, regexpPattern, validator);
		
		if (returnType.isArray())
		{
			if (returnType.getComponentType().isPrimitive())
				return new ArrayPrimitiveMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
			else
				return new ArrayMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		}
		
		if (List.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new ListStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
			else
				return new ListObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		
		if (Set.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new SetStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
			else
				return new SetObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
		
		if (Map.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new MapStringStringMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList,
				  delimiterForKeyValue);
			else
				return new MapStringObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList,
				  delimiterForKeyValue);
		
		return new CustomObjectMapper(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList,
		  delimiterForKeyValue);
	}
	
	private static DeserializationMethod determineDeserializationMethod(Class<?> customTypeOrDeserializer) throws PropertyMapException
	{
		DeserializationMethod result = null;
		HashSet<Executable> methods = new HashSet<>();
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredConstructor(String.class));
			result = DeserializationMethod.CONSTRUCTOR_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredConstructor(Map.class));
			result = DeserializationMethod.CONSTRUCTOR_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("valueOf", String.class));
			result = DeserializationMethod.VALUEOF_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("valueOf", Map.class));
			result = DeserializationMethod.VALUEOF_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("deserialize", String.class));
			result = DeserializationMethod.DESERIALIZER_STRING;
		}
		catch (NoSuchMethodException ignore) { }
		
		try
		{
			methods.add(customTypeOrDeserializer.getDeclaredMethod("deserialize", Map.class));
			result = DeserializationMethod.DESERIALIZER_MAP;
		}
		catch (NoSuchMethodException ignore) { }
		
		methods.remove(null);
		if (methods.size() > 1)
			throw new PropertyMapException("'" + customTypeOrDeserializer
			  .getName() + "' contains more than one deserialization implementation, while @BoundProperty.deserializationMethod() set to AUTO. See  class 'BoundProperty.DeserializationMethod' for more details.")
			  .logAndReturn();
		
		return result;
	}
	
	private static Executable getMethodForType(Class<?> customTypeOrDeserializer, DeserializationMethod deserializationMethod) throws
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
			if (customTypeOrDeserializer == String.class)
				executable = String.class.getDeclaredMethod("valueOf", Object.class);
			
			if (executable == null && customTypeOrDeserializer == Character.class)
				executable = CharacterMapper.class.getDeclaredMethod("valueOf", String.class);
			
			if (executable == null && ParserFactory.isPrimitiveWrapper(customTypeOrDeserializer) || customTypeOrDeserializer.isEnum())
				executable = customTypeOrDeserializer.getDeclaredMethod("valueOf", String.class);
			
			if (executable == null && customTypeOrDeserializer == URL.class)
				executable = URL.class.getConstructor(String.class);
			
			if (executable == null && customTypeOrDeserializer == URI.class)
				executable = URI.class.getConstructor(String.class);
			
			if (executable == null && InetAddress.class.isAssignableFrom(customTypeOrDeserializer))
				executable = InetAddress.class.getDeclaredMethod("getByName", String.class);
			
			if (executable == null && customTypeOrDeserializer == UUID.class)
				executable = UUID.class.getMethod("fromString", String.class);
			
			//if (customType == YourType.class)
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
