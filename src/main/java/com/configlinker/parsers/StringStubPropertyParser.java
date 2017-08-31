package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


final class StringStubPropertyParser implements PropertyParser<String> {
	@Override
	public String parse(String value, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException {
		if (regexpPattern != null && !regexpPattern.matcher(value).matches())
			throw new PropertyMatchException("Property '" + value + "' don't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();

		return value;
	}
}
