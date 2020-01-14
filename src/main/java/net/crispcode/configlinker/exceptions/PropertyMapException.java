package net.crispcode.configlinker.exceptions;


public class PropertyMapException extends ConfigLinkerRuntimeException {

	public PropertyMapException(String message) {
		super(message);
	}

	public PropertyMapException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyMapException(Throwable cause) {
		super(cause);
	}
}
