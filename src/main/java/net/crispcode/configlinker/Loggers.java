package net.crispcode.configlinker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Loggers
{
	static final String mainLoggerName = "ConfigLinker";
	
	private static Logger MAIN_LOGGER = LoggerFactory.getLogger(Loggers.mainLoggerName);
	
	private Loggers()
	{
	}
	
	public static Logger getMainLogger()
	{
		return MAIN_LOGGER;
	}
}
