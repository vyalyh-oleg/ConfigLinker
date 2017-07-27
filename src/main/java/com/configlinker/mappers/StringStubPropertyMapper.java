package com.configlinker.mappers;

import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.regex.Pattern;


final class StringStubPropertyMapper extends AbstractPropertyMapper<String, String> {
	StringStubPropertyMapper(PropertyParser<String> propertyParser, Executable executable, Pattern regexpPattern) {
		super(propertyParser, executable, regexpPattern, null, null, null);
	}

	@Override
	protected String mapFrom(String valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
