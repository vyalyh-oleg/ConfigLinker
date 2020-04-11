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

/**
 * <p>Whether or not to ignore leading and trailing getWhitespaces for configuration names and values.<br>
 * This behaviour concerns single parameter values, every value in lists, every key and value in maps.<br>
 * <p>Default: {@link Whitespaces#INHERIT}. Also see: {@link FactorySettingsBuilder#setWhitespaces(Whitespaces)}
 * <p>Examples:
 * <pre>
 * 'color = green '
 *     if ignore: value is "green"
 *     if not ignore: value is "green "
 *
 * 'color = green, blue '
 *     if ignore: values is "green" and "blue"
 *     if not ignore: values is "green" and " blue "
 *
 * 'color = one: green, two :blue ,three : red '
 *     if ignore: keys/values are "one":"green", "two":"blue", "three":"red"
 *     if not ignore: keys/values are "one":" green", " two ":"blue ", "three ":" red "
 * </pre>
 */
public enum Whitespaces
{
	/**
	 * The behavior depends on the superior value.<br>
	 * {@code @BoundProperty --> ConfigSetFactory}
	 */
	INHERIT,
	/**
	 * Ignore getWhitespaces in keys and values. It is default value in {@link FactorySettingsBuilder#setWhitespaces(Whitespaces)}
	 */
	IGNORE,
	/**
	 * Do NOT ignore getWhitespaces in keys and values.
	 */
	ACCEPT
}
