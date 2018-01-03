package com.configlinker.exceptions;

import com.configlinker.Loggers;


public abstract class ConfigLinkerException extends Exception {
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

	public ConfigLinkerException logAndReturn() {
		Loggers.getMainLogger().error(this.getMessage(), this);
		return this;
	}
}
