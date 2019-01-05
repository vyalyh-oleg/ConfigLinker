package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;


final class MapPropertyParser implements PropertyParser<Map<String, String>>
{
	@Override
	public Map<String, String> parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList,
		String delimiterForKeyValue) throws PropertyMatchException
	{
		String[] rawValues = rawValue.split(Pattern.quote(delimiterForList));
		
		if (ignoreWhitespaces)
			for (int i = 0; i < rawValues.length; i++)
			{
				rawValues[i] = rawValues[i].trim();
			}
		
		LinkedHashMap<String, String> values = new LinkedHashMap<>();
		String escapedDelimiterForKeyValue = Pattern.quote(delimiterForKeyValue);
		for (String rawEntry : rawValues)
		{
			String[] entry = rawEntry.split(escapedDelimiterForKeyValue);
			String key, value;
			if (ignoreWhitespaces)
			{
				key = entry[0].trim();
				value = entry[1].trim();
			}
			else
			{
				key = entry[0];
				value = entry[1];
			}
			
			values.put(key, value);
			
			if (regexpPattern != null && !regexpPattern.matcher(value).matches())
			{
				throw new PropertyMatchException(
					"Property value '" + value + "' for key '" + key + "' doesn't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();
			}
		}
		
		return values;
	}
}
