package net.crispcode.configlinker.exceptions;


public class PropertyMatchException extends ConfigLinkerRuntimeException {
	public PropertyMatchException(String message) {
		super(message);
	}

	public PropertyMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyMatchException(Throwable cause) {
		super(cause);
	}
}
