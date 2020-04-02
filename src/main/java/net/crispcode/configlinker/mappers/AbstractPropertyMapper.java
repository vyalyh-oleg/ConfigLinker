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

import net.crispcode.configlinker.IDeserializer;
import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

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
	@SuppressWarnings("unchecked")
	public final MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException
	{
		// rawStringValue is NotNull
		RAW_TYPE rawTypeObj = propertyParser.parse(rawStringValue, ignoreWhitespaces, this.regexpPattern, delimiterForList, delimiterForKeyValue);
		MAPPED_TYPE mappedValue = mapFrom(rawTypeObj);
		
		if (mappedValue == null)
		{
			throw new PropertyMapException(
				"Cannot create mapped value from raw string '" + rawStringValue + "' for method '" + this.executable.getDeclaringClass()
					.getName() + "::" + this.executable.getName() + "'.").logAndReturn();
		}
		
		if (this.validator != null)
		{
			try
			{
				if (mappedValue.getClass().isArray())
					Arrays.stream((Object[]) mappedValue).forEach(this.validator::validate);
				
				else if (List.class.isAssignableFrom(this.returnType))
					((List<?>) mappedValue).forEach(this.validator::validate);
				
				else if (Set.class.isAssignableFrom(this.returnType))
					((Set<?>) mappedValue).forEach(this.validator::validate);
				
				else if (Map.class.isAssignableFrom(this.returnType))
					((Map<?, ?>) mappedValue).entrySet().stream().map(entry -> new Object[]{entry.getKey(), entry.getValue()})
						.forEach(this.validator::validate);
				else
					this.validator.validate(mappedValue);
			}
			catch (PropertyValidateException e)
			{
				throw e;
			}
			catch (Exception e1)
			{
				throw new PropertyValidateException(e1.getMessage(), e1).logAndReturn();
			}
		}
		
		return mappedValue;
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
				Constructor<? extends IDeserializer> constructor =
					(Constructor<? extends IDeserializer>) this.executable.getDeclaringClass().getDeclaredConstructor();
				constructor.setAccessible(true);
				IDeserializer deserizlizerInstance = constructor.newInstance();
				returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(deserizlizerInstance, elementValue);
			}
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
		{
			throw new PropertyMapException(
				"Cannot create object for return type in method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.", e)
				.logAndReturn();
		}
		
		if (returnElement == null)
			throw new PropertyMapException(
				"Cannot create object for return type in method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.")
				.logAndReturn();
		
/*
		// TODO: move from mapFromString() method?
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
