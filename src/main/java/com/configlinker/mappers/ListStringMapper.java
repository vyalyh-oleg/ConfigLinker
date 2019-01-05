package com.configlinker.mappers;

import com.configlinker.IPropertyValidator;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyValidateException;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.regex.Pattern;


final class ListStringMapper extends ListObjectMapper<String>
{
	ListStringMapper(Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern,
		IPropertyValidator<String> validator, String delimiterForList)
	{
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList);
	}
	
	@Override
	protected List<String> mapFrom(List<String> valueFromParser) throws PropertyValidateException, PropertyMapException
	{
		return valueFromParser;
	}
}
