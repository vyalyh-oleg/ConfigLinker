package com.configlinker;

import com.configlinker.exceptions.ConfigLinkerRuntimeException;

import java.util.Collections;
import java.util.Map;


final public class ConfigChangedEvent {
	private final Class<?> configInterface;
	private final String sourcePath;
	private final Map<String, ValuesPair> rawValues;
	private final ConfigLinkerRuntimeException exception;

	public ConfigChangedEvent(Class<?> configInterface, String sourcePath, Map<String, ValuesPair> rawValues, ConfigLinkerRuntimeException exception) {
		this.configInterface = configInterface;
		this.sourcePath = sourcePath;
		this.rawValues = Collections.unmodifiableMap(rawValues);
		this.exception = exception;
	}

	public Class<?> getConfigInterface() {
		return configInterface;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 *
	 * @return Keys and values (old, new) that were changed. Can be null (if the error happened before new properties have been loaded).
	 */
	public Map<String, ValuesPair> getRawValues() {
		return rawValues;
	}

	/**
	 * If an error occurred during updating configuration. The exception with the reason will be here.
	 * In such case ${@link #getRawValues()} can return null (if the error happened before new properties have been loaded).
	 */
	public ConfigLinkerRuntimeException getException() {
		return exception;
	}

	public static class ValuesPair {
		private String oldValue;
		private String newValue;

		public ValuesPair(String oldValue, String newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public String getOldValue() {
			return oldValue;
		}

		public String getNewValue() {
			return newValue;
		}
	}
}
