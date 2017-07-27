package com.configlinker.exceptions;


public class ConfigLinkerRuntimeException extends RuntimeException {
	private ConfigLinkerRuntimeException() {
		super();
	}

	public ConfigLinkerRuntimeException(String message) {
		super(message);
	}

	public ConfigLinkerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigLinkerRuntimeException(Throwable cause) {
		super(cause);
	}
}
