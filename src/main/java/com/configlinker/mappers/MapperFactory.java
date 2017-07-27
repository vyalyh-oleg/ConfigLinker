package com.configlinker.mappers;

import com.configlinker.PropertyValidator;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.AnnotationAnalyzeException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.parsers.ParserFactory;
import com.configlinker.parsers.PropertyParser;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public final class MapperFactory {

	private MapperFactory() {
	}

	@SuppressWarnings("unchecked")
	public static PropertyMapper create(Class<?> returnType, BoundProperty boundPropertyAnnotation, Method propertyMethod) throws AnnotationAnalyzeException, PropertyMapException {

		String strRegexpPattern = boundPropertyAnnotation.regexpPattern();
		Pattern regexpPattern = null;
		if (strRegexpPattern.length() > 0)
			regexpPattern = Pattern.compile(strRegexpPattern);

		Class<? extends PropertyValidator> validator_class = boundPropertyAnnotation.validator();
		PropertyValidator validator = null;
		if (validator_class != PropertyValidator.class)
			try {
				validator = validator_class.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO: log and exception
			}

		Class<?> customTypeOrDeserializer = boundPropertyAnnotation.customType();
		BoundProperty.DeserializationMethod deserializationMethod = boundPropertyAnnotation.deserializationMethod();

		String delimiterForList = boundPropertyAnnotation.delimiterForList();
		String delimiterForKeyValue = boundPropertyAnnotation.delimiterForKeyValue();

		// --------------------------------------------------------------------------------

		if (returnType.isPrimitive()) {
			customTypeOrDeserializer = ParserFactory.getWrapperForPrimitive(returnType);
			deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		}

		if (ParserFactory.isPrimitiveWrapper(returnType) || returnType == String.class || returnType.isEnum()) {
			customTypeOrDeserializer = returnType;
			deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		}

		if (returnType.isArray()) {
			Class<?> arrayType = returnType.getComponentType();
			if (arrayType.isPrimitive())
				arrayType = ParserFactory.getWrapperForPrimitive(arrayType);

			if (ParserFactory.isPrimitiveWrapper(arrayType) || arrayType == String.class || arrayType.isEnum()) {
				customTypeOrDeserializer = arrayType;
				deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
			}

			if (customTypeOrDeserializer == Object.class)
				customTypeOrDeserializer = arrayType;
		}

		if (customTypeOrDeserializer.isPrimitive()) {
			// TODO: log
			throw new AnnotationAnalyzeException("Value of '@BoundProperty.customType()' can not be a primitive type class, but current value is '" + customTypeOrDeserializer.getName() + "' and current return type is '" + returnType.getName() + "'.");
		}

		if (customTypeOrDeserializer == Object.class && (List.class.isAssignableFrom(returnType) || Set.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType))) {
			// TODO: log
			throw new PropertyMapException("For type '" + returnType.getName() + "' you must specify it generic type in '@BoundProperty.customType' and choose '@BoundProperty.deserializationMethod'; method leading to error: '" + propertyMethod.getDeclaringClass().getName() + "." + propertyMethod.getName() + "()'.");
		}

		// --------------------------------------------------------------------------------

		PropertyParser propertyParser = ParserFactory.create(returnType, deserializationMethod);
		Executable executable = getMethodForType(customTypeOrDeserializer, deserializationMethod);

		// TODO: additional check for 'customTypeOrDeserializer', if return type is List, Set or Map
		executable.setAccessible(true);

		if (returnType == String.class)
			return new StringStubPropertyMapper(propertyParser, executable, regexpPattern);

		if (returnType.isArray())
			if (customTypeOrDeserializer == String.class)
				return new ArrayStringMapper(propertyParser, executable, regexpPattern, delimiterForList);
			else
				return new ArrayMapper(propertyParser, executable, regexpPattern, validator, delimiterForList);

		if (List.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new ListStringMapper(propertyParser, executable, regexpPattern, delimiterForList);
			else
				return new ListObjectMapper(propertyParser, executable, regexpPattern, validator, delimiterForList);

		if (Set.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new SetStringMapper(propertyParser, executable, regexpPattern, delimiterForList);
			else
				return new SetObjectMapper(propertyParser, executable, regexpPattern, validator, delimiterForList);

		if (Map.class.isAssignableFrom(returnType))
			if (customTypeOrDeserializer == String.class)
				return new MapStringStringMapper(propertyParser, executable, regexpPattern, delimiterForList, delimiterForKeyValue);
			else
				return new MapStringObjectMapper(propertyParser, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);

		return new CustomObjectMapper(propertyParser, executable, regexpPattern, validator, delimiterForList, delimiterForKeyValue);
	}

	private static Executable getMethodForType(Class<?> customTypeOrDeserializer, BoundProperty.DeserializationMethod deserializationMethod) throws PropertyMapException {
		try {
			Executable executable = getMethodForPredefinedType(customTypeOrDeserializer);
			if (executable != null)
				return executable;

			// This is done in order to remove the implementation details out of the public api
			switch (deserializationMethod) {
				case CONSTRUCTOR_STRING:
					return customTypeOrDeserializer.getDeclaredConstructor(String.class);
				case CONSTRUCTOR_MAP:
					return customTypeOrDeserializer.getDeclaredConstructor(Map.class);
				case VALUEOF_STRING:
					return customTypeOrDeserializer.getDeclaredMethod("valueOf", String.class);
				case VALUEOF_MAP:
					return customTypeOrDeserializer.getDeclaredMethod("valueOf", Map.class);
				case DESERIALIZER_STRING:
					return customTypeOrDeserializer.getDeclaredMethod("deserialize", String.class);
				case DESERIALIZER_MAP:
					return customTypeOrDeserializer.getDeclaredMethod("deserialize", Map.class);
				default:
					// TODO: log and throw
					throw new PropertyMapException("There is no suitable version for @BoundProperty.DeserializationMethod='" + deserializationMethod.name() + "'; custom type or deserializer: '" + customTypeOrDeserializer.getName() + "'.");
			}
		} catch (NoSuchMethodException e) {
			// TODO: log and throw
			throw new PropertyMapException("Can not find deserialization method in: '" + customTypeOrDeserializer.getName() + "'; @BoundProperty.DeserializationMethod='" + deserializationMethod.name() + "'.", e);
		}
	}

	private static Executable getMethodForPredefinedType(Class<?> customTypeOrDeserializer) throws NoSuchMethodException {
		Executable executable = null;
		if (customTypeOrDeserializer == Character.class)
			executable = customTypeOrDeserializer.getDeclaredMethod("charAt", int.class);

		if (customTypeOrDeserializer == String.class)
			executable = customTypeOrDeserializer.getDeclaredMethod("valueOf", Object.class);

		//if (customTypeOrDeserializer == String.class)

		// TODO: implement for other types
		// maybe it will be needed to change AbstractPropertyMapper.createObject() method.
		// URL, URI, InetAddress

		return executable;
	}
}
