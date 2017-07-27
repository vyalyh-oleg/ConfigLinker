package com.configlinker.exceptions;


public class PropertyNotFoundException extends ConfigLinkerRuntimeException {
	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}
}
