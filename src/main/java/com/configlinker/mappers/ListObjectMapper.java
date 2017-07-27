package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


class ListObjectMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, List<ELEMENT>> {
	ListObjectMapper(PropertyParser<List<String>> propertyParser, Executable executable, Pattern regexpPattern, PropertyValidator validator, String delimiterForList) {
		super(propertyParser, executable, regexpPattern, validator, delimiterForList, null);
	}

	@Override
	protected List<ELEMENT> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		ArrayList<ELEMENT> list = new ArrayList<>();
		valueFromParser.stream().map(this::<String, ELEMENT>createObject).forEach(list::add);
		return list;
	}
}
