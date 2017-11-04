package com.configlinker.mappers;


import com.configlinker.PropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class MapStringObjectMapper<VALUE> extends AbstractPropertyMapper<Map<String, String>, Map<String, VALUE>> {
	MapStringObjectMapper(Class<?> returnType, PropertyParser<Map<String, String>> propertyParser, Executable executable, Pattern regexpPattern, PropertyValidator validator, String delimiterForList, String delimiterForKeyValue) {
		super(returnType, propertyParser, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected Map<String, VALUE> mapFrom(Map<String, String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		HashMap<String, VALUE> hashMap = new HashMap<>();
		valueFromParser.forEach((key, value) -> hashMap.put(key, createObject(value)));
		return hashMap;
	}
}
