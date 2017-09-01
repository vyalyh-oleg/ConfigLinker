package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.regex.Pattern;


class ArrayMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, ELEMENT[]> {
	ArrayMapper(PropertyParser<List<String>> propertyParser, Executable executable, Pattern regexpPattern, PropertyValidator validator, String delimiterForList) {
		super(propertyParser, executable, regexpPattern, validator, delimiterForList, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ELEMENT[] mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return (ELEMENT[]) valueFromParser.stream().map(this::<String, ELEMENT>createObject).toArray();
	}
}
