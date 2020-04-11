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


package net.crispcode.configlinker.mappers;


import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;
import net.crispcode.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;


class MapStringObjectMapper<VALUE> extends AbstractPropertyMapper<Map<String, String>, Map<String, VALUE>> {
	MapStringObjectMapper( Class<?> returnType, PropertyParser<Map<String, String>> propertyParser, boolean ignoreWhitespaces, Executable executable, Pattern regexpPattern, IPropertyValidator<VALUE> validator, String delimiterForList, String delimiterForKeyValue) {
		super(returnType, propertyParser, ignoreWhitespaces, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	@Override
	protected Map<String, VALUE> mapFrom(Map<String, String> valueFromParser)  throws PropertyValidateException, PropertyMapException {
		LinkedHashMap<String, VALUE> hashMap = new LinkedHashMap<>();
		valueFromParser.forEach((key, value) -> hashMap.put(key, createObject(value)));
		return hashMap;
	}
}
