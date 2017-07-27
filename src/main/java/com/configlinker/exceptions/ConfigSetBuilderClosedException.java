package com.configlinker.exceptions;


public class ConfigSetBuilderClosedException extends ConfigLinkerRuntimeException {
	private static final String message = "You can not change parameter or add property to closed ConfigSetBuilder. It is closed, because it was already used in ConfigSetFactory.";

	private static final ConfigSetBuilderClosedException instance = new ConfigSetBuilderClosedException();

	private ConfigSetBuilderClosedException() {
		super(ConfigSetBuilderClosedException.message);
	}

	public static ConfigSetBuilderClosedException getInstance(){
		return instance;
	}
}
