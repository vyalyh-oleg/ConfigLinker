package com.configlinker.annotations;

import com.configlinker.FactorySettingsBuilder;
import com.configlinker.IDeserializer;
import com.configlinker.enums.DeserializationMethod;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.IPropertyValidator;
import com.configlinker.enums.Whitespaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundProperty {
	/**
	 * <p>Should contain string representation of property name that used in configuration.</p>
	 * <p>If you set {@code @BoundObject.propertyNamePrefix} it will be added before this value, and <b>in that case the name of the value should begin with a dot</b>. If it begins from any other acceptable symbols except dot, the value considered as full name and be used without {@code propertyNamePrefix}.</p>
	 * <p>You can use variables for substituting some parts of the name.
	 * <br>The name of variable can consist of letters, numbers and underscores.
	 * <br>Variables should be set in {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>
	 * Example:
	 * <pre>	".configuration.<b>${type}</b>.memory.limit"</pre>
	 * Where the {@code 'type'} can be for example "test" or "production", etc.
	 * <p>
	 * <br>
	 * <p>Name can also contain dynamic variables which can be used in runtime. For using this ability you must declare property method that accepts desired number of {@code String} or {@code Enum} arguments.</p>
	 * Example:
	 * <pre>	".configuration.<b>${type}</b>.<b>@{group}</b>.limit.<b>@{border}</b>"</pre>
	 * The method might look like this:
	 * <pre>	getServerLimitFor(String group, String border);</pre>
	 * Where the {@code 'group'} can be "memory", "disk", "cpu", and {@code 'border'} -- "max", "min", "default".
	 * <p>
	 * <p><b>IMPORTANT:</b>
	 * <br>To use dynamic variables your interfaces must be compiled with {@code "javac -parameters"} argument.
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
	String regex() default "";

	/**
	 * <p>
	 * Delimiter for parameter value, which is treated as {@code List} or key-value pairs in {@code Map}. Default value is comma - {@code ','}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = one,two,three}</pre>
	 * @return -
	 */
	String delimList() default ",";

	/**
	 * <p>
	 * Delimiter between key-value pair for {@code Map}. Default value is colon - {@code ':'}.
	 * <p>
	 * Example:
	 * <pre>	{@code param.name.in.file = color:red,number:two,target:method}</pre>
	 * @return -
	 */
	String delimKeyValue() default ":";
	
	/**
	 * <p>Whether or not to ignore leading and trailing getWhitespaces for configuration names and values.
	 * <p>See: {@link Whitespaces}
	 * @return -
	 */
	Whitespaces whitespaces() default Whitespaces.INHERIT;
	
	/**
	 * <p>Default value is {@code Object.class}, which is mean automatic detection (see next paragraph).</p>
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
	 * <br>
	 * <p>If you want to use <b>custom return type (or array of custom types)</b>, you must just implement deserialization logic for your type, and no need any changes in the current parameter.
	 * <p>If you want to use <b>custom return type as generic type</b> for {@code List}, {@code Set} or {@code Map} (only for value), you must just properly specify it's generic type in the angle brackets and implement deserialization logic.
	 * <br>
	 * <br>
	 * <p> <b>Only if the deserialization method resides not in your custom type</b>, place here it class ({@code customType = YourDeserializer.class}).
	 * <br>
	 * <br>
	 * <p> For the last two cases you must implement at least one of the deserialization methods, described in {@link DeserializationMethod}.
	 * @return -
	 */
	Class<?> customType() default Object.class;

	/**
	 * <p>If you want to use custom return type, you must just implement deserialization logic for it (see {@link #customType()}) and point this choice here.
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
	 * Default value is {@link ErrorBehavior#INHERIT} and specified in {@link FactorySettingsBuilder#setErrorBehavior(ErrorBehavior)}
	 * @return -
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERIT;
}
