package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class ListObjectMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, List<ELEMENT>> {
	ListObjectMapper( Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<ELEMENT> validator, String delimiterForList) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, null);
	}

	@Override
	protected List<ELEMENT> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser.stream().map(this::<String, ELEMENT>createObject).collect(Collectors.toList());
	}
}
