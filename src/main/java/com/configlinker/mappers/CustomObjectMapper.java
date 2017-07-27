package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.regex.Pattern;


final class CustomObjectMapper<RAW_TYPE, MAPPED_TYPE> extends AbstractPropertyMapper<RAW_TYPE, MAPPED_TYPE> {
	CustomObjectMapper(PropertyParser<RAW_TYPE> propertyParser, Executable executable, Pattern regexpPattern, PropertyValidator validator, String delimiterForList, String delimiterForKeyValue) {
		super(propertyParser, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected MAPPED_TYPE mapFrom(RAW_TYPE valueFromParser) throws PropertyValidateException, PropertyMapException {
		return createObject(valueFromParser);
	}
}
