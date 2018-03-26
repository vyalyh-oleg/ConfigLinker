package com.configlinker;

import java.util.Map;


public interface IDeserializer<T> {
	default T deserialize(Map<String,String> rawValue) { return null; };
	default T deserialize(String rawValue) { return null; };
}
