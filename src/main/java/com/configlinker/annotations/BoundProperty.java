package com.configlinker.annotations;

import com.configlinker.ConfigSetBuilder;
import com.configlinker.Deserializer;
import com.configlinker.ErrorBehavior;
import com.configlinker.PropertyValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;


@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundProperty {
	/**
	 * <p>Should contain string representation of property name that used in configuration.</p>
	 * <p>If you set {@code @BoundObject.propertyNamePrefix} it will be added before this value, and in that case this value should begin with a dot. If it begins from any other acceptable symbols except dot, the value considered as full name and used without {@code propertyNamePrefix}.</p>
	 * <p>
	 * You can use variables for substituting some parts of the name.
	 * Variables can be set in {@link com.configlinker.ConfigSetBuilder#addParameter(String, String)}.
	 * <p>
	 * Example:
	 * <pre>	".configuration.${type}.memory.limit"</pre>
	 * Where the {@code 'type'} can be for example "test" or "production", etc.
	 * <p>
	 * <p>Name can also contain dynamic variables which can be used in runtime. For using this ability you must declare property getter method that accepts desired number of {@code String} or {@code Enum} arguments.</p>
	 * Example:
	 * <pre>	".configuration.${type}.@{group}.limit.@{border}"</pre>
	 * The method might look like this:
	 * <pre>	getServerLimitFor(String group, String border);</pre>
	 * Where the {@code 'group'} can be "memory", "disk", "cpu", and {@code 'border'} -- "max", "min", "default".
	 */
	String name();

	/**
	 * Regex pattern for checking raw text value, read from configuration.
	 * For primitives single value is checked, and for collection types (list, map, enum) every element value (as string) is checked.
	 * By defaults regex check does not used.
	 */
	String regexpPattern() default "";

	/**
	 * Delimiter for parameter value, which is treated as {@code List}. Default value is comma - {@code ','}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = one,two,three}</pre>
	 */
	String delimiterForList() default ",";

	/**
	 * Delimiter for parameter element, which is treated as key-value pair for {@code Map}. Default value is colon - {@code ':'}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = color:red,number:two,target:method}</pre>
	 */
	String delimiterForKeyValue() default ":";

	/**
	 * <p>Default value is {@code Object.class}, which used for standard types (see next paragraph).</p>
	 * <p><b>Standard supported return types</b> (you shouldn't change current parameter for them because it will be ignored):</p>
	 * <ul>
	 * <li>all primitives and arrays of primitives;</li>
	 * <li>all wrappers of primitives and arrays of them;</li>
	 * <li>{@code String} and {@code String[]}</li>
	 * <li>{@code Enum} and {@code Enum[]}</li>
	 * <li>{@code List<String>}</li>
	 * <li>{@code Set<String>}</li>
	 * <li>{@code Map<String,String>}</li>
	 * </ul>
	 * <p>If you want to use custom return type (or array of custom types), you must just implement deserialization logic for it and point choice in {@link #deserializationMethod()}. And no need any changes in the current parameter.
	 * <p>If you want to use custom return type as generic type for {@code List}, {@code Set} or {@code Map} (only for value), you must indicate here it {@code Class}.
	 * <p>If you write own deserializer implementation of {@link Deserializer}, it {@code Class} must be specified here instead of return type class.
	 * <p>
	 * <p>Pay attention, you have different ways to implement deserialization logic, which are described in ({@link DeserializationMethod}):
	 * <ul>
	 * <li>write special constructor for your class, see {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#CONSTRUCTOR_MAP};</li>
	 * <li>write static instance generator method in your class, see {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#VALUEOF_MAP};</li>
	 * <li>write separate deserializer class, see {@link DeserializationMethod#DESERIALIZER_STRING}, {@link DeserializationMethod#DESERIALIZER_MAP}.</li>
	 * </ul>
	 * <p>If return type for you configuration method is List, Set or Map, then only {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#DESERIALIZER_STRING} allowed as deserialization method for it's values.
	 */
	Class<?> customType() default Object.class;

	/**
	 * <p>If you want to use custom return type, you must just implement deserialization logic for it (see {@link #customType()}) and point this choice here.
	 * <p>Default value is {@link DeserializationMethod#CONSTRUCTOR_STRING}
	 * <p>If return type for you configuration method is List, Set or Map, then only {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#DESERIALIZER_STRING} allowed as deserialization method for it's values.
	 */
	DeserializationMethod deserializationMethod() default DeserializationMethod.CONSTRUCTOR_STRING;

	/**
	 * <p>Custom validator for returned value. By default no validators are used.
	 * <p>If you need additional checks just implement {@link PropertyValidator} interface and point class here.
	 */
	Class<? extends PropertyValidator> validator() default PropertyValidator.class;

	/**
	 * What to do if the property value not exists in underlying persistent store.
	 * Default value is {@link ErrorBehavior#INHERITED} and specified in {@link ConfigSetBuilder#setErrorBehavior(ErrorBehavior)}
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERITED;

	/**
	 * Values:
	 * <ul>
	 * <li>{@link #CONSTRUCTOR_STRING}</li>
	 * <li>{@link #CONSTRUCTOR_MAP} - default for {@link #deserializationMethod()}</li>
	 * <li>{@link #VALUEOF_STRING}</li>
	 * <li>{@link #VALUEOF_MAP}</li>
	 * <li>{@link #DESERIALIZER_STRING}</li>
	 * <li>{@link #DESERIALIZER_MAP}</li>
	 * </ul>
	 * <p>If return type for you configuration method is List, Set or Map, then only {@link #CONSTRUCTOR_STRING}, {@link #VALUEOF_STRING}, {@link #DESERIALIZER_STRING} allowed as deserialization method for it's values.
	 */
	enum DeserializationMethod {
		/**
		 * Chose this variant if you implement special constructor for your class:
		 * <p>{@code 'public/private <CustomReturnType>(String raw)'}
		 */
		CONSTRUCTOR_STRING,
		/**
		 * Chose this variant if you implement special constructor for your class:
		 * <p>{@code 'public/private <CustomReturnType>(Map<String,String> raw)'}
		 */
		CONSTRUCTOR_MAP,
		/**
		 * Chose this variant if you implement in your custom type static instance generator method:
		 * <p>{@code 'public/private static <CustomReturnType> valueOf(String raw)'}
		 */
		VALUEOF_STRING,
		/**
		 * Chose this variant if you implement in your custom type static instance generator method:
		 * <p>{@code 'public/private static <CustomReturnType> valueOf(Map<String,String> raw)'}
		 */
		VALUEOF_MAP,
		/**
		 * Chose this variant if you implement {@link Deserializer#deserialize(String)}.
		 */
		DESERIALIZER_STRING,
		/**
		 * Chose this variant if you implement {@link Deserializer#deserialize(Map)}.
		 */
		DESERIALIZER_MAP
		;
	}
}
