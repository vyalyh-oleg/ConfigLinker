package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


final class MapPropertyParser implements PropertyParser<Map<String, String>> {
	@Override
	public Map<String, String> parse(String value, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException {

		String[] rawValues = value.split(Pattern.quote(delimiterForList));
		HashMap<String, String> values = new HashMap<>();
		String escapedDelimiterForKeyValue = Pattern.quote(delimiterForKeyValue);
		for (String rawEntry : rawValues) {
			String[] entry = rawEntry.split(escapedDelimiterForKeyValue);
			values.put(entry[0], entry[1]);
		}

		if (regexpPattern != null)
			for (String elementValue : values.values())
				if (!regexpPattern.matcher(elementValue).matches())
					throw new PropertyMatchException("Property '" + value + "' don't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();

		return values;
	}
}
