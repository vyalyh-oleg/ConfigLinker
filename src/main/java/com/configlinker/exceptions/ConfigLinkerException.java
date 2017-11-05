package com.configlinker.exceptions;

import com.configlinker.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ConfigLinkerException extends Exception {
	private static Logger MAIN_LOGGER = LoggerFactory.getLogger(Loggers.mainLogger);

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
		MAIN_LOGGER.error(this.getMessage(), this);
		return this;
	}
}
