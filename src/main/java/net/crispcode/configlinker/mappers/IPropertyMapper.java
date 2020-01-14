package net.crispcode.configlinker.mappers;


import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;

public interface IPropertyMapper<MAPPED_TYPE>
{
	MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException;
}
