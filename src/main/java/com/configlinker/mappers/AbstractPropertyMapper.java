package com.configlinker.mappers;

import com.configlinker.IDeserializer;
import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


abstract class AbstractPropertyMapper<RAW_TYPE, MAPPED_TYPE> implements IPropertyMapper<MAPPED_TYPE>
{
	protected final PropertyParser<RAW_TYPE> propertyParser;
	protected final boolean ignoreWhitespaces;
	protected final Class<?> returnType;
	protected final String delimiterForList;
	protected final String delimiterForKeyValue;
	protected final Executable executable;
	protected final Pattern regexpPattern;
	protected final IPropertyValidator validator;
	
	AbstractPropertyMapper(Class<?> returnType, PropertyParser<RAW_TYPE> propertyParser, boolean ignoreWhitespaces, Executable executable,
	  Pattern regexpPattern, IPropertyValidator validator, String delimiterForList, String delimiterForKeyValue)
	{
		this.returnType = returnType;
		this.propertyParser = propertyParser;
		this.ignoreWhitespaces = ignoreWhitespaces;
		this.delimiterForList = delimiterForList;
		this.delimiterForKeyValue = delimiterForKeyValue;
		this.regexpPattern = regexpPattern;
		this.executable = executable;
		this.validator = validator;
	}
	
	@Override
	public final MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException
	{
		// TODO: move regex checker here
		
		MAPPED_TYPE mappedValue = mapFrom(propertyParser.parse(rawStringValue, ignoreWhitespaces, this.regexpPattern, delimiterForList, delimiterForKeyValue));
		
		// TODO: implement errorBehaviour
		if (mappedValue == null)
			throw new PropertyMapException(
			  "Cannot create mapped value from raw string '" + rawStringValue + "' for method '" + this.executable.getDeclaringClass()
				.getName() + "::" + this.executable.getName() + "'.").logAndReturn();
		
		if (this.validator != null)
		{
			if (mappedValue.getClass().isArray())
				Arrays.stream((Object[]) mappedValue).forEach(this.validator::validate);
			
			else if (List.class.isAssignableFrom(this.returnType))
				((List<?>) mappedValue).stream().forEach(this.validator::validate);
			
			else if (Set.class.isAssignableFrom(this.returnType))
				((Set<?>) mappedValue).stream().forEach(this.validator::validate);
			
			else if (Map.class.isAssignableFrom(this.returnType))
				((Map<?, ?>) mappedValue).entrySet().stream().map(entry -> new Object[]{entry.getKey(), entry.getValue()}).forEach(this.validator::validate);
			else
				this.validator.validate(mappedValue);
		}
		
		return mappedValue;
	}
	
	protected final <RETURN> RETURN createObjectFromString(String elementStringValue)
	{
		// TODO: additional check for generic type (customTypeOrDeserializer), if return type is List, Set or Map
		// TODO: additional mapper for each element if it is complex type
		//PropertyParser propertyParser = ParserFactory.create(returnType, deserializationMethod);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	final <SRC_TYPE, RETURN_TYPE> RETURN_TYPE createObject(SRC_TYPE elementValue) throws PropertyValidateException, PropertyMapException
	{
		RETURN_TYPE returnElement = null;
		try
		{
			this.executable.setAccessible(true);
			
			if (this.executable instanceof Constructor)
				returnElement = ((Constructor<RETURN_TYPE>) this.executable).newInstance(elementValue);
			
			// implementation for specific type methods
			if (returnElement == null)
				returnElement = getReturnElementForPredefinedType(elementValue);
			
			if (returnElement == null && Modifier.isStatic(this.executable.getModifiers()))
				returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(null, elementValue);
			
			if (returnElement == null && IDeserializer.class.isAssignableFrom(this.executable.getDeclaringClass()))
			{
				Constructor<? extends IDeserializer> constructor = (Constructor<? extends IDeserializer>) this.executable.getDeclaringClass().getDeclaredConstructor();
				constructor.setAccessible(true);
				IDeserializer deserizlizerInstance = constructor.newInstance();
				returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(deserizlizerInstance, elementValue);
			}
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
		{
			// TODO: implement errorBehaviour
			throw new PropertyMapException(
			  "Cannot create object for return type in method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.", e)
			  .logAndReturn();
		}
		
		// TODO: implement errorBehaviour
		if (returnElement == null)
			throw new PropertyMapException(
			  "Cannot create object for return type in method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.")
			  .logAndReturn();
		
/*
		// TODO: implement errorBehaviour
		if (this.validator != null)
			this.validator.validate(returnElement);
*/
		
		return returnElement;
	}
	
	// maybe it will be needed to change MapperFactory.getReturnElementForPredefinedType() method.
	@SuppressWarnings("unchecked")
	private <SRC_TYPE, RETURN_TYPE> RETURN_TYPE getReturnElementForPredefinedType(SRC_TYPE elementValue) throws InvocationTargetException,
	  IllegalAccessException
	{
		RETURN_TYPE returnElement;
		
		// an example of how to use non predefined methods for specific type
/*
		if (this.executable.getDeclaringClass() == String.class && this.executable.getName().equals("charAt"))
		{
			returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(elementValue, 0);
			return returnElement;
		}
*/
		
		return null;
	}
	
	abstract protected MAPPED_TYPE mapFrom(RAW_TYPE valueFromParser) throws PropertyValidateException, PropertyMapException;
}
