package net.crispcode.configlinker;

import java.util.Map;

/**
 *
 * @param <T>
 */
public interface IDeserializer<T>
{
	default T deserialize(Map<String,String> stringValues) { return null; }
	default T deserialize(String rawValue) { return null; }
}
