package com.configlinker;

import com.configlinker.exceptions.PropertyValidateException;


public interface IPropertyValidator<T> {
	void validate(T value) throws PropertyValidateException;
}
