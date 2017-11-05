package com.configlinker.exceptions;

import com.configlinker.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ConfigLinkerRuntimeException extends RuntimeException {
	private static Logger MAIN_LOGGER = LoggerFactory.getLogger(Loggers.mainLogger);

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
		MAIN_LOGGER.error(this.getMessage(), this);
		return this;
	}
}
