package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


final class ListPropertyParser implements PropertyParser<List<String>> {
	@Override
	public List<String> parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException {
		String[] values = rawValue.split(Pattern.quote(delimiterForList));

		for (int i = 0; i < values.length; i++) {
			if (ignoreWhitespaces)
				values[i] = values[i].trim();

			if (regexpPattern != null && !regexpPattern.matcher(values[i]).matches())
				throw new PropertyMatchException("Property '" + values[i] + "' don't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();
		}

		return Arrays.stream(values).collect(Collectors.toList());
	}
}
