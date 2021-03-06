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


package net.crispcode.configlinker;

import java.util.Map;

/**
 *
 * @param <CUSTOM_TYPE> is your custom type, that the deserializer will return
 */
public interface IDeserializer<CUSTOM_TYPE>
{
	default CUSTOM_TYPE deserialize(Map<String,String> stringValues) { return null; }
	default CUSTOM_TYPE deserialize(String rawValue) { return null; }
	default <ARG_TYPE> CUSTOM_TYPE deserialize(ARG_TYPE boundObject) { return null; }
}
