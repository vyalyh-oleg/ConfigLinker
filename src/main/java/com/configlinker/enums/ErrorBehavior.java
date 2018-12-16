package com.configlinker.enums;


import com.configlinker.FactorySettingsBuilder;

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
	 * <p>Throw {@link com.configlinker.exceptions.PropertyNotFoundException} when the value for configuration property not found. It is default value when {@link #INHERIT}.
	 */
	THROW_EXCEPTION,
	/**
	 * <p>Return null, when the value for configuration property not found. Applicable to reference types.
	 */
	RETURN_NULL
	;
}
