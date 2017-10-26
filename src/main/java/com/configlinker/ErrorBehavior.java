package com.configlinker;


/**
 * What to do if the property value does not exist in underlying persistent store.
 */
public enum ErrorBehavior {
	INHERITED,
	/**
	 * Throw {@link com.configlinker.exceptions.PropertyNotFoundException} when the value for configuration property not found.
	 */
	THROW_EXCEPTION,
	/**
	 * Return null, when the value for configuration property not found. Applicable to reference types.
	 */
	RETURN_NULL
	;
}
