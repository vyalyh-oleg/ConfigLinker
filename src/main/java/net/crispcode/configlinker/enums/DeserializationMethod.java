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


package net.crispcode.configlinker.enums;

import net.crispcode.configlinker.IDeserializer;
import net.crispcode.configlinker.annotations.BoundProperty;

import java.util.Map;

/**
 * <p>Values:
 * <ul>
 * <li>{@link #AUTO} - default for {@link BoundProperty#deserializationMethod()}</li>
 * <li>{@link #CONSTRUCTOR_STRING}</li>
 * <li>{@link #CONSTRUCTOR_MAP}</li>
 * <li>{@link #VALUEOF_STRING}</li>
 * <li>{@link #VALUEOF_MAP}</li>
 * <li>{@link #DESERIALIZER_STRING}</li>
 * <li>{@link #DESERIALIZER_MAP}</li>
 * </ul>
 * <p>By default, the appropriate deserialization method will be tried to found out automatically.
 * <p>If your class implements multiple deserialization variants, you must choose appropriate value manually.
 * <p>If return type for you configuration method is List, Set or Map, then only {@link #CONSTRUCTOR_STRING}, {@link #VALUEOF_STRING}, {@link #DESERIALIZER_STRING} allowed as deserialization method for it's values.
 * <br>
 * <br>
 * <p><b>See also:</b> {@link BoundProperty#customType()} and {@link BoundProperty#deserializationMethod()}</p>
 *
 */
public enum DeserializationMethod
{
	/**
	 * <p>The default value. The appropriate deserialization method will be tried to found out automatically.
	 * <p>If you occasionally implement multiple deserialization variants, the exception will be thrown.
	 */
	AUTO,
	/**
	 * <p>Chose this variant if you implement special constructor for your class:
	 * <p>{@code 'public/private CustomReturnType(String raw)'}
	 */
	CONSTRUCTOR_STRING,
	/**
	 * <p>Chose this variant if you implement special constructor for your class:
	 * <p>{@code 'public/private CustomReturnType(Map<String,String> raw)'}
	 */
	CONSTRUCTOR_MAP,
	/**
	 * <p>Chose this variant if you implement in your custom type static instance generator method:
	 * <p>{@code 'public/private static CustomReturnType valueOf(String raw)'}
	 */
	VALUEOF_STRING,
	/**
	 * <p>Chose this variant if you implement in your custom type static instance generator method:
	 * <p>{@code 'public/private static CustomReturnType valueOf(Map<String,String> raw)'}
	 */
	VALUEOF_MAP,
	/**
	 * <p>Chose this variant if your class implements interface {@link IDeserializer} and method {@link IDeserializer#deserialize(String rawValue)}.
	 */
	DESERIALIZER_STRING,
	/**
	 * <p>Chose this variant if your class implements interface {@link IDeserializer} and method {@link IDeserializer#deserialize(Map stringValues)}.
	 */
	DESERIALIZER_MAP
	;
}
