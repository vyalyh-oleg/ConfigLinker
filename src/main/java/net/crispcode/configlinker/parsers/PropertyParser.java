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


package net.crispcode.configlinker.parsers;

import net.crispcode.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


public interface PropertyParser<RAW_TYPE>
{
	RAW_TYPE parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws
		PropertyMatchException;
}
