package net.crispcode.configlinker.parsers;

import net.crispcode.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


final class StringStubPropertyParser implements PropertyParser<String>
{
	@Override
	public String parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws
		PropertyMatchException
	{
		if (ignoreWhitespaces)
			rawValue = rawValue.trim();
		
		if (regexpPattern != null && !regexpPattern.matcher(rawValue).matches())
			throw new PropertyMatchException("Property value '" + rawValue + "' doesn't match pattern '" + regexpPattern.toString() + "'.").logAndReturn();
		
		return rawValue;
	}
}
