package net.crispcode.configlinker.parsers;

import net.crispcode.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


public interface PropertyParser<RAW_TYPE>
{
	RAW_TYPE parse(String rawValue, boolean ignoreWhitespaces, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws
		PropertyMatchException;
}
