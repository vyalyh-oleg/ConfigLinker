package com.configlinker.exceptions;


public class ConfigLinkerException extends Exception {
	private ConfigLinkerException() {
		super();
	}

	public ConfigLinkerException(String message) {
		super(message);
	}

	public ConfigLinkerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigLinkerException(Throwable cause) {
		super(cause);
	}
}
