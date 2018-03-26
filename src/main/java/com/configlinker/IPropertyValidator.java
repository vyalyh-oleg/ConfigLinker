package com.configlinker;

import com.configlinker.exceptions.PropertyValidateException;


public interface IPropertyValidator<T> {
	boolean validate(T value) throws PropertyValidateException;
}
