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


import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;

public interface IPropertyMapper<MAPPED_TYPE>
{
	MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException;
}
