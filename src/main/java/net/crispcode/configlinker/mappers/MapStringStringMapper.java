package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.Map;
import java.util.regex.Pattern;


final class MapStringStringMapper extends MapStringObjectMapper<String> {
	MapStringStringMapper(Class<?> returnType, PropertyParser<Map<String, String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<String> validator, String delimiterForList, String delimiterForKeyValue) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected Map<String, String> mapFrom(Map<String, String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		return valueFromParser;
	}
}
