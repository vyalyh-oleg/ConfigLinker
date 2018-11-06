package com.configlinker.mappers;

import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


final class SetStringMapper extends SetObjectMapper<String> {
	SetStringMapper(Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<String> validator, String delimiterForList) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
	}

	@Override
	protected Set<String> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return new LinkedHashSet<>(valueFromParser);
	}
}
