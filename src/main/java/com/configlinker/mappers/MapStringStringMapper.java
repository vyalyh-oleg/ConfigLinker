package com.configlinker.mappers;

import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.Map;
import java.util.regex.Pattern;


final class MapStringStringMapper extends MapStringObjectMapper<String> {
	MapStringStringMapper(PropertyParser<Map<String, String>> propertyParser, Executable executable, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) {
		super(propertyParser, executable, regexpPattern, null, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected Map<String, String> mapFrom(Map<String, String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
