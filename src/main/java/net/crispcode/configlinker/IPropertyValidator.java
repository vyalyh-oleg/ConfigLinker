package net.crispcode.configlinker;

import net.crispcode.configlinker.exceptions.PropertyValidateException;


/**
 * For creation custom validators for returned value. Validates configuration value in their object form.
 * @param <T>
 */
public interface IPropertyValidator<T>
{
	void validate(T value) throws PropertyValidateException;
}
