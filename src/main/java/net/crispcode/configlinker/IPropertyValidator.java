package net.crispcode.configlinker;

import net.crispcode.configlinker.exceptions.PropertyValidateException;


public interface IPropertyValidator<T>
{
	void validate(T value) throws PropertyValidateException;
}
