package com.configlinker.parsers;

import com.configlinker.exceptions.PropertyMatchException;

import java.util.regex.Pattern;


public interface PropertyParser<RAW_TYPE> {
	RAW_TYPE parse(String value, Pattern regexpPattern, String delimiterForList, String delimiterForKeyValue) throws PropertyMatchException;
}
