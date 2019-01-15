package com.configlinker.annotations;

import com.configlinker.FactorySettingsBuilder;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.IConfigChangeListener;
import com.configlinker.enums.SourceScheme;
import com.configlinker.enums.TrackPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.Charset;


@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundObject {

	/**
	 * <p>
	 * Describe the type of the source that used to retrieve property values for this annotated object.
	 * @return -
	 */
	SourceScheme sourceScheme() default SourceScheme.INHERIT;

	/**
	 * <p>Must point to source where data can be found to fill in this object with necessary values.
	 * <p>You can use variables for substituting some parts of this path.
	 * <br>Variables can be set in {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>Path could be relative to the current working directory or to the root of classpath (depends on {@link SourceScheme}).
	 * <p>Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endPart"</pre>
	 * @return -
	 */
	String sourcePath();

	/**
	 * <p>
	 * Headers that are used to make requests to get configuration parameters (if the {@link BoundObject#sourceScheme} is {@link SourceScheme#HTTP}). These values are merged with values which you can set with {@link FactorySettingsBuilder}.
	 * <p>
	 * You can use variables for substituting some parts of this path.
	 * Variables can be set in {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>
	 * Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endName"</pre>
	 * @return -
	 */
	String[] httpHeaders() default "";

	/**
	 * <p>This value is used to retrieve {@code Charset} object invoking {@link java.nio.charset.Charset#forName(String)}, and then it will be used to load configuration in raw text format.
	 * <p>Default value inherited from {@link FactorySettingsBuilder#setCharset(Charset)} and equal {@code StandardCharsets.UTF_8}.
	 * <p>If you set any value here, it will be used instead default value.
	 * @return -
	 */
	String charsetName() default "";

	/**
	 * <p>The common names part of parameters group that is used to bind with methods of this annotated interface.<br>
	 * If it is not specified you can use only full parameter names in {@link BoundProperty#name()}.<br>
	 * If it has any value then both variants, full and prefix-aware can be used.</p>
	 *
	 * <p>Example:<br>
	 * If this parameter for example is set to {@code "mycompany"}, then the {@code @BoundProperty.name()} can be as {@code ".serverName"} (start with dot is obligatory). This means, the final parameter name, which will be searched in property file, is {@code "mycompany.serverName"}.<br>
	 * Without dot at the beginning {@code @BoundProperty.name()} is considered as full parameter name and the prefix is not taken into account.
	 *
	 * <p>
	 * You can also use variables for substituting some parts of this prefix.
	 * Variables should be set with {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>
	 * Example:
	 * <pre>	"servers.${type}.srv1.configuration"</pre>
	 * Where the {@code type} can be for example "test" or "production", etc.:
	 * <pre>	"servers.<b>test</b>.srv1.configuration"</pre>
	 * @return -
	 */
	String propertyNamePrefix() default "";

	/**
	 * <p>Default value is {@link TrackPolicy#INHERIT} which mean global policy will be used (specify in method {@link FactorySettingsBuilder#setTrackPolicy(TrackPolicy)}). To override such behaviour choose {@link TrackPolicy#DISABLE} or {@link TrackPolicy#ENABLE} value.
	 * @return -
	 */
	TrackPolicy trackingPolicy() default TrackPolicy.INHERIT;

	/**
	 * <p>Used only if here or in {@link FactorySettingsBuilder#setTrackPolicy(TrackPolicy)} specified {@link TrackPolicy#ENABLE}, and the {@link SourceScheme#HTTP}.
	 * <p>Otherwise this parameter ignored.
	 * <p>Default value is '0' which mean inherited behaviour (will be used value from {@code FactorySettingsBuilder} (equal '60' seconds).
	 * <p>MIN value = 15 seconds, MAX value = 86400 seconds (1 day = 24 hours * 3600 seconds).
	 * @return -
	 */
	int trackingInterval() default 0;

	/**
	 *
	 * @return -
	 */
	Class<? extends IConfigChangeListener> changeListener() default IConfigChangeListener.class;

	/**
	 * <p>
	 * What to do if the property value does not exist in underlying persistent store.
	 * Default value is {@link ErrorBehavior#INHERIT} and specified in {@link FactorySettingsBuilder#setErrorBehavior(ErrorBehavior)}
	 * @return -
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERIT;
}
