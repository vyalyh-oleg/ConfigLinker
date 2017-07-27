package com.configlinker;

import com.configlinker.exceptions.PropertyValidateException;


public interface PropertyValidator<T> {
	boolean validate(T value) throws PropertyValidateException;
}
