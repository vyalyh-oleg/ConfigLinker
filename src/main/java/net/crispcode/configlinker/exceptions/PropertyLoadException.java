package net.crispcode.configlinker.exceptions;


public class PropertyLoadException extends ConfigLinkerRuntimeException {
	public PropertyLoadException(String message) {
		super(message);
	}

	public PropertyLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyLoadException(Throwable cause) {
		super(cause);
	}
}
