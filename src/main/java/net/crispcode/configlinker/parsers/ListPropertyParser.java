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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


final class ListPropertyParser implements PropertyParser<List<String>>
{
	@Override
	public List<String> parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws
		PropertyMatchException
	{
		String[] values = rawValue.split(Pattern.quote(delimiterForList));
		
		for (int i = 0; i < values.length; i++)
		{
			if (ignoreWhitespaces)
				values[i] = values[i].trim();
			
			if (regexpPattern != null && !regexpPattern.matcher(values[i]).matches())
				throw new PropertyMatchException("Property value '" + values[i] + "' doesn't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();
		}
		
		return Arrays.stream(values).collect(Collectors.toList());
	}
}
