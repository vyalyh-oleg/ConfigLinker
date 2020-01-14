package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.regex.Pattern;


final class StringStubPropertyMapper extends AbstractPropertyMapper<String, String> {
	StringStubPropertyMapper(PropertyParser<String> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<String> validator) {
		super(String.class, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, null, null);
	}

	@Override
	protected String mapFrom(String valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
