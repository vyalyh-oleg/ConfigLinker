package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.regex.Pattern;


final class CustomObjectMapper<RAW_TYPE, MAPPED_TYPE> extends AbstractPropertyMapper<RAW_TYPE, MAPPED_TYPE> {
	CustomObjectMapper( Class<?> returnType, PropertyParser<RAW_TYPE> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<MAPPED_TYPE> validator, String delimiterForList, String delimiterForKeyValue) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected MAPPED_TYPE mapFrom(RAW_TYPE valueFromParser) throws PropertyValidateException, PropertyMapException {
		return createObject(valueFromParser);
	}
}
