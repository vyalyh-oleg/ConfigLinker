package com.configlinker.mappers;

import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.regex.Pattern;


final class ListStringMapper extends ListObjectMapper<String> {
	ListStringMapper(PropertyParser<List<String>> propertyParser, Executable executable, Pattern regexpPattern, String delimiterForList) {
		super(propertyParser, executable, regexpPattern, null, delimiterForList);
	}

	@Override
	protected List<String> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
