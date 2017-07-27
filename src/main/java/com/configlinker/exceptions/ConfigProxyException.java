package com.configlinker.exceptions;


public class ConfigProxyException extends ConfigLinkerRuntimeException {
	public ConfigProxyException(String message) {
		super(message);
	}

	public ConfigProxyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigProxyException(Throwable cause) {
		super(cause);
	}
}
