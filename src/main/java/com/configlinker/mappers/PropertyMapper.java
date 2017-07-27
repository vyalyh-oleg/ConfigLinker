package com.configlinker.mappers;


import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyValidateException;

public interface PropertyMapper<MAPPED_TYPE> {
	MAPPED_TYPE mapFromString(String rawStringValue) throws PropertyMatchException, PropertyValidateException, PropertyMapException;
}
