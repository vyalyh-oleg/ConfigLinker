package com.configlinker.mappers;

import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.regex.Pattern;


final class StringStubPropertyMapper extends AbstractPropertyMapper<String, String> {
	StringStubPropertyMapper(PropertyParser<String> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<String> validator) {
		super(String.class, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, null, null);
	}

	@Override
	protected String mapFrom(String valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
