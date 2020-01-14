package net.crispcode.configlinker.exceptions;


public class PropertyValidateException extends ConfigLinkerRuntimeException {
	public PropertyValidateException(String message) {
		super(message);
	}

	public PropertyValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyValidateException(Throwable cause) {
		super(cause);
	}
}
