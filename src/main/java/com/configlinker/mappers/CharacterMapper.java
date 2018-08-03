package com.configlinker.mappers;


final class CharacterMapper
{
	static char valueOf(String raw)
	{
		if (raw.length() > 1)
			throw new IllegalArgumentException("Given string '" + raw + "' instead of a single char.");
		return raw.charAt(0);
	}
}
