package net.crispcode.configlinker.annotations;

import net.crispcode.configlinker.FactorySettingsBuilder;
import net.crispcode.configlinker.enums.DeserializationMethod;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.IPropertyValidator;
import net.crispcode.configlinker.enums.Whitespaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Every method declaration in interface that expected to return the particular value from the `properties` file should have such an annotation.
 */
@Target(value  = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundProperty {
	/**
	 * <p>Should contain string representation of property name that is used in configuration.</p>
	 * <p>If you set {@code @BoundObject.propertyNamePrefix} the prefix will be added before the {@code name}, and <b>in that case the name of the value should begin with a dot</b>. If it begins from any other acceptable symbols except dot, the value considered as full name and be used without {@code propertyNamePrefix}.</p>
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
	 * <p>Regex pattern for checking raw text value, read from configuration.<br/>
	 * For simple types single value is checked, and for collection types (arrays, list, map, enum) every element value (as string) is checked.<br/>
	 * By defaults regex check does not used.
	 * <p><br/>
	 * If validation fails the {@code PropertyMatchException} will be thrown.
	 * Its message and the hierarchy of 'cause' contains additional information about error.
	 * <p><br/>
	 * The checking process performs on the call of {@code ConfigSetFactory.create()} method.
	 * <bThe only exception</b> is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
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
	 * <p>Default value is {@code Object.class}, which means automatic detection for standard supported return types.</p>
	 * <p><b>So you no need to change anything if you use them:</b></p>
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
	 * <p>If you want to use <b>custom return type as generic type</b> for {@code List<CustomType>}, {@code Set<CustomType>} or {@code Map<String,CustomType>}, you must just properly specify it's generic type in the angle brackets and implement deserialization logic.
	 * <br>
	 * <p><b>Deserialization logic</b> should be encapsulated in one of the methods, described in {@link DeserializationMethod} enum. Then set your choice in {@link BoundProperty#deserializationMethod}.
	 * <br>
	 * <br>
	 * <p> <b>Only if the deserialization method resides <u>not in your custom type</u></b>, but in other place (other class), set here it class:
	 * <p> {@code customType = YourDeserializer.class}.
	 * @return -
	 */
	Class<?> customType() default Object.class;

	/**
	 * <p>If you want to use custom return type, you must just implement deserialization logic for it (see {@link #customType()}) and point the type of deserialization method here.
	 * <p>Default value is {@link DeserializationMethod#AUTO}
	 * <p>By default, the appropriate deserialization method will be tried to found out automatically.
	 * <p>If your class implements multiple deserialization variants, you must choose appropriate value manually.
	 *
	 * <p>If the return type for you configuration method is List, Set or Map (thus your custom type is a generic parameter), then only {@link DeserializationMethod#CONSTRUCTOR_STRING}, {@link DeserializationMethod#VALUEOF_STRING}, {@link DeserializationMethod#DESERIALIZER_STRING} allowed as deserialization method for custom type.
	 * @return -
	 */
	DeserializationMethod deserializationMethod() default DeserializationMethod.AUTO;

	/**
	 * <p>Custom validator for returned value. Validate configuration value in their object form.
	 * <p>By default validators are not used.
	 * <p>If you need additional checks just implement {@link IPropertyValidator} interface and point class here.
	 * <p><br/>
	 * If validation fails the {@code PropertyValidateException} will be thrown.
	 * Its message and the hierarchy of 'cause' contains additional information about error.
	 * <p><br/>
	 * The checking process performs on the call of {@code ConfigSetFactory.create()} method.
	 * <bThe only exception</b> is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
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
