package net.crispcode.configlinker.exceptions;


public class FactoryConfigBuilderClosedException extends ConfigLinkerRuntimeException
{
	private static final String message = "You can not change parameter or add property to closed FactorySettingsBuilder. It is closed, because it was already used in ConfigSetFactory.";
	
	private static final FactoryConfigBuilderClosedException instance = new FactoryConfigBuilderClosedException();
	
	private FactoryConfigBuilderClosedException()
	{
		super(FactoryConfigBuilderClosedException.message);
	}
	
	public static FactoryConfigBuilderClosedException getInstance()
	{
		return instance;
	}
}
