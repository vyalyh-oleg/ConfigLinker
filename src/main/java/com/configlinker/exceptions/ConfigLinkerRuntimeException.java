package com.configlinker.exceptions;

import com.configlinker.Loggers;


public abstract class ConfigLinkerRuntimeException extends RuntimeException {
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

	public ConfigLinkerRuntimeException logAndReturn() {
		Loggers.getMainLogger().error(this.getMessage(), this);
		return this;
	}
}
