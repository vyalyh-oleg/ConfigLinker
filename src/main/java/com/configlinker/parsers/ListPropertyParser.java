package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


final class ListPropertyParser implements PropertyParser<List<String>> {
	@Override
	public List<String> parse(String value, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException {
		String[] values = value.split(Pattern.quote(delimiterForList));

		if (regexpPattern != null)
			for (String elementValue : values) {
				if (!regexpPattern.matcher(elementValue).matches()) {
					// TODO: log and throw
					throw new PropertyMatchException("");
				}

			}

		return Arrays.stream(values).collect(Collectors.toList());
	}
}
