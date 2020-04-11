/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <crispcode.net@gmail.com>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.parsers;

import net.crispcode.configlinker.exceptions.PropertyMatchException;

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
