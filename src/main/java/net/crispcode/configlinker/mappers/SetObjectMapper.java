/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

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
