package net.crispcode.configlinker.mappers;

import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


class SetObjectMapper<ELEMENT> extends AbstractPropertyMapper<List<String>, Set<ELEMENT>> {
	SetObjectMapper( Class<?> returnType, PropertyParser<List<String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<ELEMENT> validator, String delimiterForList) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, null);
	}

	@Override
	protected Set<ELEMENT> mapFrom(List<String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		LinkedHashSet<ELEMENT> set = new LinkedHashSet<>();
		valueFromParser.stream().map(this::<String, ELEMENT>createObject).forEach(set::add);
		return set;
	}
}
