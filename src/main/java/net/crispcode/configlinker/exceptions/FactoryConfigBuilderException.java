package net.crispcode.configlinker.exceptions;


public class FactoryConfigBuilderException extends ConfigLinkerRuntimeException
{
	public FactoryConfigBuilderException(String message)
	{
		super(message);
	}
	
	public FactoryConfigBuilderException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public FactoryConfigBuilderException(Throwable cause)
	{
		super(cause);
	}
}
