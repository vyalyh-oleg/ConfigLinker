package com.configlinker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Loggers {
	public static final String mainLoggerName = "ConfigLinker";
	private static Logger MAIN_LOGGER = LoggerFactory.getLogger(Loggers.mainLoggerName);

	private Loggers() {
	}

	public static Logger getMainLogger()
	{
		return MAIN_LOGGER;
	}
}
