package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


class StringStubPropertyParser implements PropertyParser<String> {
	@Override
	public String parse(String value, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException {
		if (regexpPattern != null && !regexpPattern.matcher(value).matches()) {
			// TODO: log and throw
			throw new PropertyMatchException("");
		}
		return value;
	}
}
