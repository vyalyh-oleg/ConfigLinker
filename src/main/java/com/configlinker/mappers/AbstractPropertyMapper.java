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
import java.util.regex.Pattern;


abstract class AbstractPropertyMapper<RAW_TYPE, MAPPED_TYPE> implements PropertyMapper<MAPPED_TYPE> {
	protected final PropertyParser<RAW_TYPE> propertyParser;
	private final boolean ignoreWhitespaces;
	protected final Class<?> returnType;
	private String delimiterForList;
	private String delimiterForKeyValue;
	protected final Executable executable;
	protected final Pattern regexpPattern;
	private final IPropertyValidator validator;

	AbstractPropertyMapper( Class<?> returnType, PropertyParser<RAW_TYPE> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator validator, String delimiterForList, String delimiterForKeyValue) {
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
	public final MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException {
		MAPPED_TYPE mappedValue = mapFrom(propertyParser.parse(rawStringValue, ignoreWhitespaces, this.regexpPattern, delimiterForList, delimiterForKeyValue));
		if (mappedValue == null)
			throw new PropertyMapException("Cannot create mapped value from raw string '" + rawStringValue + "' for method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.").logAndReturn();

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
	final <SRC_TYPE, RETURN_TYPE> RETURN_TYPE createObject(SRC_TYPE elementValue) throws PropertyValidateException, PropertyMapException {
		RETURN_TYPE returnElement;
		try {
			if (IDeserializer.class.isAssignableFrom( this.executable.getDeclaringClass())) {
				Object deserizlizerInstance = this.executable.getDeclaringClass().newInstance();
				returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(deserizlizerInstance, elementValue);
			}

			// implementation for specific type methods
			returnElement = getMethodForPredefinedType(elementValue);
			if (returnElement != null)
				return returnElement;

			if (this.executable instanceof Constructor)
				returnElement = ((Constructor<RETURN_TYPE>) this.executable).newInstance(elementValue);

			if (Modifier.isStatic(this.executable.getModifiers()))
				returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(null, elementValue);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new PropertyMapException("Cannot interpret return type for method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.", e).logAndReturn();
		}

		if (returnElement == null)
			throw new PropertyMapException("Cannot interpret return type for method '" + this.executable.getDeclaringClass().getName() + "::" + this.executable.getName() + "'.").logAndReturn();

		if (this.validator != null)
			this.validator.validate(returnElement);

		return returnElement;
	}

	// maybe it will be needed to change MapperFactory.getMethodForPredefinedType() method.
	@SuppressWarnings("unchecked")
	private <SRC_TYPE, RETURN_TYPE> RETURN_TYPE getMethodForPredefinedType(SRC_TYPE elementValue) throws InvocationTargetException, IllegalAccessException {
		RETURN_TYPE returnElement;

		if (this.executable.getDeclaringClass() == String.class && this.executable.getName().equals("charAt")) {
			returnElement = (RETURN_TYPE) ((Method) this.executable).invoke(elementValue, 0);
			return returnElement;
		}

		return null;
	}

	abstract protected MAPPED_TYPE mapFrom(RAW_TYPE valueFromParser) throws PropertyValidateException, PropertyMapException;
}
