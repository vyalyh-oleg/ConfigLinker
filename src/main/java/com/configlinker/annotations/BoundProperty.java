package com.configlinker.annotations;

import com.configlinker.IDeserializer;
import com.configlinker.ErrorBehavior;
import com.configlinker.FactoryConfigBuilder;
import com.configlinker.IPropertyValidator;

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
	 * Variables can be set in {@link FactoryConfigBuilder#addParameter(String, String)}.
	 * <p>
	 * Example:
	 * <pre>	".configuration.${type}.memory.limit"</pre>
	 * Where the {@code 'type'} can be for example "test" or "production", etc.
	 * <p>
	 * <br>
	 * <p>Name can also contain dynamic variables which can be used in runtime. For using this ability you must declare property getter method that accepts desired number of {@code String} or {@code Enum} arguments.</p>
	 * Example:
	 * <pre>	".configuration.${type}.@{group}.limit.@{border}"</pre>
	 * The method might look like this:
	 * <pre>	getServerLimitFor(String group, String border);</pre>
	 * Where the {@code 'group'} can be "memory", "disk", "cpu", and {@code 'border'} -- "max", "min", "default".
	 * @return -
	 */
	String name();

	/**
	 * <p>
	 * Regex pattern for checking raw text value, read from configuration.
	 * For primitives single value is checked, and for collection types (list, map, enum) every element value (as string) is checked.
	 * By defaults regex check does not used.
	 * @return -
	 */
	String regexPattern() default "";

	/**
	 * <p>
	 * Delimiter for parameter value, which is treated as {@code List} or key-value pairs in {@code Map}. Default value is comma - {@code ','}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = one,two,three}</pre>
	 * @return -
	 */
	String delimiterForList() default ",";

	/**
	 * <p>
	 * Delimiter between key-value pair for {@code Map}. Default value is colon - {@code ':'}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = color:red,number:two,target:method}</pre>
	 * @return -
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
	 * <p>If you want to use <b>custom return type (or array of custom types)</b>, you must just implement deserialization logic for it and point choice in {@link #deserializationMethod()}. And no need any changes in the current parameter.
	 * <br/>
	 * <p>If you want to use <b>custom return type as generic type</b> for {@code List}, {@code Set} or {@code Map} (only for value), you must indicate here it generic {@code Class}.
	 * <br/>
	 * <p>If you write own deserializer implementation of {@link IDeserializer}, it {@code Class} must be specified here instead of return type class.
	 * <p>
	 * <p>Pay attention, you have different ways to implement deserialization logic, which are described in ({@link DeserializationMethod}):
	 * <ul>
	 * <li>write special constructor for your class, see <br/>{@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#CONSTRUCTOR_MAP};</li>
	 * <li>write static instance generator method in your class, see <br/> {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#VALUEOF_MAP};</li>
	 * <li>write separate deserializer class, see <br/> {@link DeserializationMethod#DESERIALIZER_STRING}, {@link DeserializationMethod#DESERIALIZER_MAP}.</li>
	 * </ul>
	 * <p>If return type for you configuration method is List, Set or Map, then only <br/> {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#DESERIALIZER_STRING} <br/> allowed as deserialization method for their values.
	 * @return -
	 */
	Class<?> customTypeOrDeserializer() default Object.class;

	/**
	 * <p>If you want to use custom return type, you must just implement deserialization logic for it (see {@link #customTypeOrDeserializer()}) and point this choice here.
	 * <p>Default value is {@link DeserializationMethod#CONSTRUCTOR_STRING}
	 * <p>If return type for you configuration method is List, Set or Map, then only {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#DESERIALIZER_STRING} allowed as deserialization method for it's values.
	 * @return -
	 */
	DeserializationMethod deserializationMethod() default DeserializationMethod.AUTO;

	/**
	 * <p>Custom validator for returned value. By default validators are not used.
	 * <p>If you need additional checks just implement {@link IPropertyValidator} interface and point class here.
	 * @return -
	 */
	Class<? extends IPropertyValidator> validator() default IPropertyValidator.class;

	/**
	 * What to do if the property value does not exist in underlying persistent store.
	 * Default value is {@link ErrorBehavior#INHERITED} and specified in {@link FactoryConfigBuilder#setErrorBehavior(ErrorBehavior)}
	 * @return -
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERITED;

	/**
	 * <p>Values:
	 * <ul>
	 * <li>{@link #AUTO} - default for {@link #deserializationMethod()}</li>
	 * <li>{@link #CONSTRUCTOR_STRING}</li>
	 * <li>{@link #CONSTRUCTOR_MAP}</li>
	 * <li>{@link #VALUEOF_STRING}</li>
	 * <li>{@link #VALUEOF_MAP}</li>
	 * <li>{@link #DESERIALIZER_STRING}</li>
	 * <li>{@link #DESERIALIZER_MAP}</li>
	 * </ul>
	 * <p>By default, the appropriate deserialization method will be tried to found out automatically.
	 * <p>If your class implements multiple deserialization variants, you must choose appropriate value manually.
	 * <p>If return type for you configuration method is List, Set or Map, then only {@link #CONSTRUCTOR_STRING}, {@link #VALUEOF_STRING}, {@link #DESERIALIZER_STRING} allowed as deserialization method for it's values.
	 * <br>
	 * <p><b>See also:</b> {@link BoundProperty#customTypeOrDeserializer()} and {@link BoundProperty#deserializationMethod()}</p>
	 *
	 */
	enum DeserializationMethod {
		/**
		 * <p>The default value. The appropriate deserialization method will be tried to found out automatically.
		 * <p>If you occasionally implement multiple deserialization variants, the exception will be thrown.
		 */
		AUTO,
		/**
		 * <p>Chose this variant if you implement special constructor for your class:
		 * <p>{@code 'public/private <CustomReturnType>(String raw)'}
		 */
		CONSTRUCTOR_STRING,
		/**
		 * <p>Chose this variant if you implement special constructor for your class:
		 * <p>{@code 'public/private <CustomReturnType>(Map<String,String> raw)'}
		 */
		CONSTRUCTOR_MAP,
		/**
		 * <p>Chose this variant if you implement in your custom type static instance generator method:
		 * <p>{@code 'public/private static <CustomReturnType> valueOf(String raw)'}
		 */
		VALUEOF_STRING,
		/**
		 * <p>Chose this variant if you implement in your custom type static instance generator method:
		 * <p>{@code 'public/private static <CustomReturnType> valueOf(Map<String,String> raw)'}
		 */
		VALUEOF_MAP,
		/**
		 * <p>Chose this variant if your class implements interface {@link IDeserializer} and method {@link IDeserializer#deserialize(String rawValue)}.
		 */
		DESERIALIZER_STRING,
		/**
		 * <p>Chose this variant if your class implements interface {@link IDeserializer} and method {@link IDeserializer#deserialize(Map stringValues)}.
		 */
		DESERIALIZER_MAP
		;
	}
}
