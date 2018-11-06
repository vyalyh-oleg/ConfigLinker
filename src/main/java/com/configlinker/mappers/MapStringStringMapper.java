package com.configlinker.mappers;

import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.Map;
import java.util.regex.Pattern;


final class MapStringStringMapper extends MapStringObjectMapper<String> {
	MapStringStringMapper(Class<?> returnType, PropertyParser<Map<String, String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<String> validator, String delimiterForList, String delimiterForKeyValue) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected Map<String, String> mapFrom(Map<String, String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
