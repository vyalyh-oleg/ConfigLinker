package com.configlinker.parsers;

import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyParseException;

import java.util.List;
import java.util.Map;
import java.util.Set;


public final class ParserFactory {
	private static final StringStubPropertyParser STRING_STUB_PROPERTY_PARSER = new StringStubPropertyParser();
	private static final ListPropertyParser LIST_PROPERTY_PARSER = new ListPropertyParser();
	private static final MapPropertyParser MAP_PROPERTY_PARSER = new MapPropertyParser();

	private ParserFactory() {
	}

	public static PropertyParser create(Class<?> returnType, BoundProperty.DeserializationMethod deserializationMethod) throws PropertyParseException {
		if (returnType.isEnum() || returnType == String.class ||
				returnType.isPrimitive() || ParserFactory.isPrimitiveWrapper(returnType))
			return STRING_STUB_PROPERTY_PARSER;

		if (returnType.isArray())
			return LIST_PROPERTY_PARSER;

		if (List.class.isAssignableFrom(returnType))
			return LIST_PROPERTY_PARSER;

		if (Set.class.isAssignableFrom(returnType))
			return LIST_PROPERTY_PARSER;

		if (Map.class.isAssignableFrom(returnType))
			return MAP_PROPERTY_PARSER;

		if (deserializationMethod == BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING ||
				deserializationMethod == BoundProperty.DeserializationMethod.VALUEOF_STRING ||
				deserializationMethod == BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
			return STRING_STUB_PROPERTY_PARSER;

		if (deserializationMethod == BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP ||
				deserializationMethod == BoundProperty.DeserializationMethod.VALUEOF_MAP ||
				deserializationMethod == BoundProperty.DeserializationMethod.DESERIALIZER_MAP)
			return MAP_PROPERTY_PARSER;

		throw new PropertyParseException("Cannot find appropriate parser for '" + returnType.getName() + "'.").logAndReturn();
	}

	public static boolean isPrimitiveWrapper(Class<?> returnType) {
		return (returnType == Boolean.class || returnType == Byte.class || returnType == Character.class ||
				returnType == Short.class || returnType == Integer.class || returnType == Long.class ||
				returnType == Float.class || returnType == Double.class);
	}

	public static Class<?> getWrapperForPrimitive(Class<?> primitiveType) throws PropertyParseException {
		if (!primitiveType.isPrimitive())
			throw new PropertyParseException("'" + primitiveType.getName() + "' is not a primitive type.").logAndReturn();

		if (primitiveType == boolean.class)
			return Boolean.class;
		if (primitiveType == byte.class)
			return Byte.class;
		if (primitiveType == char.class)
			return Character.class;
		if (primitiveType == short.class)
			return Short.class;
		if (primitiveType == int.class)
			return Integer.class;
		if (primitiveType == long.class)
			return Long.class;
		if (primitiveType == float.class)
			return Float.class;
		if (primitiveType == double.class)
			return Double.class;

		throw new PropertyParseException("Can not found wrapper for primitive type.").logAndReturn();
	}
}
