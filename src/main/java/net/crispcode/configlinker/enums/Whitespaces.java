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
