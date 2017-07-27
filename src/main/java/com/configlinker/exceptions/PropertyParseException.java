package com.configlinker.exceptions;


public class PropertyParseException extends ConfigLinkerRuntimeException {
	public PropertyParseException(String message) {
		super(message);
	}

	public PropertyParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyParseException(Throwable cause) {
		super(cause);
	}
}
