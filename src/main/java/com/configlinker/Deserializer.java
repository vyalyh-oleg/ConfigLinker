package com.configlinker;


import java.util.Map;

public interface Deserializer<T> {
	T deserialize(Map<String,String> rawValue);
	T deserialize(String rawValue);
}
