package com.configlinker.mappers;

import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.regex.Pattern;


class ArrayMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, ELEMENT[]> {
	ArrayMapper( Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<ELEMENT> validator, String delimiterForList) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected ELEMENT[] mapFrom(List<String> valueFromParser) throws PropertyValidateException, PropertyMapException {
		Object[] array = valueFromParser.stream().map(this::<String, ELEMENT>createObject).toArray();
		Object returnArray = Array.newInstance(returnType.getComponentType(), array.length);
		for (int i = 0; i < array.length; i++)
			Array.set(returnArray, i, array[i]);
		return (ELEMENT[]) returnArray;
	}
}
