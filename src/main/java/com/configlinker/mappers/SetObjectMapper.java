package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


class SetObjectMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, Set<ELEMENT>> {
	SetObjectMapper(Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, PropertyValidator validator, String delimiterForList) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, null);
	}

	@Override
	protected Set<ELEMENT> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		LinkedHashSet<ELEMENT> set = new LinkedHashSet<>();
		valueFromParser.stream().map(this::<String, ELEMENT>createObject).forEach(set::add);
		return set;
	}
}
