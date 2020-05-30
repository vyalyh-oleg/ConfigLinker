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


package net.crispcode.configlinker.enums;


import net.crispcode.configlinker.FactorySettingsBuilder;
import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.exceptions.PropertyNotFoundException;


/**
 * <p>What to do if the property value does not exist in underlying persistent store or cannot be converted to object representation for any reasons.
 * <p>This is useful only in two cases:<br>
 * <ul>
 * <li>if ConfigLinker library found changes in one of the tracked property file during runtime and doesn't able to deal with them;</li>
 * <li>if the property has runtime dynamic parts (in other words this is mean that methods in your interfaces have parameters) and somewhere in other code the non-existent part was passed to this method.</li>
 * </ul>
 */
public enum ErrorBehavior {
	/**
	 * Inherits value from {@link FactorySettingsBuilder#setErrorBehavior(ErrorBehavior)}. Default value {@link #THROW_EXCEPTION}.
	 */
	INHERIT,
	/**
	 * <p>Throws {@link PropertyNotFoundException} when the value for configuration property wasn't found. It is default behaviour when {@link #INHERIT}.
	 */
	THROW_EXCEPTION,
	/**
	 * <p>Returns null, when the value for configuration property not found. Applicable to reference types.
	 */
	RETURN_NULL,
	/**
	 * <p>Tries to get the value from the source under {@link BoundObject#defaultSourcePath()}. If it doesn't exist, returns null.</p>
	 */
	TRY_DEFAULTS_OR_NULL,
	/**
	 * <p>Tries to get the value from the source under {@link BoundObject#defaultSourcePath()}. If it doesn't exist, throws exception {@link PropertyNotFoundException}.</p>
	 */
	TRY_DEFAULTS_OR_EXCEPTION;
}
