package com.configlinker.annotations;

import com.configlinker.ConfigChangeListener;
import com.configlinker.ConfigSetBuilder;
import com.configlinker.ErrorBehavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.Charset;


@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundObject {

	/**
	 * Describe the type of the source that used to retrieve property values for this annotated object.<br/>
	 */
	SourceScheme sourceScheme() default SourceScheme.INHERIT;

	/**
	 * Must point to source where data can be found to fill in this object with necessary values.
	 * <p>
	 * You can use variables for substituting some parts of this path.
	 * Variables can be set in {@link ConfigSetBuilder#addParameter(String, String)}.<br/>
	 * Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endPart"</pre>
	 */
	String sourcePath();

	/**
	 * Headers that are used to make requests to get configuration parameters (if the {@link BoundObject#sourceScheme} is {@link SourceScheme#HTTP}). These values are merged with values which you can set with {@link ConfigSetBuilder}.
	 * <p>
	 * You can use variables for substituting some parts of this path.
	 * Variables can be set in {@link ConfigSetBuilder#addParameter(String, String)}.<br/>
	 * Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endName"</pre>
	 */
	String[] httpHeaders() default "";

	/**
	 * <p>This value is used to retrieve {@code Charset} object invoking {@link java.nio.charset.Charset#forName(String)}, and then it will be used to load configuration in raw text format.
	 * <p>Default value inherited from {@link ConfigSetBuilder#setCharset(Charset)} and equal {@code StandardCharsets.UTF_8}.
	 * <p>If you set any value here, it will be used instead default value.
	 */
	String charsetName() default "";

	/**
	 * <p>The common names part of parameters group that is used to bind with methods of this annotated object. If it is not specified you should use full parameter names in {@code @BoundPropert.propertyName}.</p><br/>
	 * <p>
	 * You can use variables for substituting some parts of this prefix.
	 * Variables should be set with {@link ConfigSetBuilder#addParameter(String, String)}.<br/>
	 * Example:
	 * <pre>	"servers.${type}.srv1.configuration"</pre>
	 * Where the {@code type} can be for example "test" or "production", etc.:<br/>
	 * <pre>	"servers.<b>test</b>.srv1.configuration"</pre>
	 * <p>
	 */
	String propertyNamePrefix() default "";

	/**
	 * <p>Default value is {@link TrackPolicy#INHERIT} which mean global policy will be used (specify in method {@link ConfigSetBuilder#setTrackPolicy(TrackPolicy)}). To override such behaviour choose {@link TrackPolicy#DISABLE} or {@link TrackPolicy#ENABLE} value.
	 */
	TrackPolicy trackPolicy() default TrackPolicy.INHERIT;

	/**
	 * <p>Used only if here or in {@link ConfigSetBuilder#setTrackPolicy(TrackPolicy)} specified {@link TrackPolicy#ENABLE}, and the {@link SourceScheme#HTTP}.
	 * <p>Otherwise this parameter ignored.
	 * <p>Default value is '0' which mean inherited behaviour (will be used value from {@code ConfigSetBuilder} (equal '60' seconds).
	 * <p>MIN value = 15 seconds, MAX value = 86400 seconds (24 hours = 24*3600 seconds).
	 */
	int trackingInterval() default 0;

	/**
	 *
	 */
	Class<? extends ConfigChangeListener> changeListener() default ConfigChangeListener.class;

	/**
	 * What to do if the property value not exists in underlying persistent store.
	 * Default value is {@link ErrorBehavior#INHERITED} and specified in {@link ConfigSetBuilder#setErrorBehavior(ErrorBehavior)}
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERITED;

	/**
	 * Scheme describe how to access the data source.
	 */
	enum SourceScheme {
		/**
		 * Mean that concrete value will be set by {@link ConfigSetBuilder#setSourceScheme(SourceScheme)}, and it is {@link SourceScheme#FILE} by default.
		 */
		INHERIT,
		CLASSPATH,
		FILE,
		HTTP,
		CONFIG_LINKER_SERVER
		;
	}

	/**
	 * Policy for refresh configuration parameters.
	 */
	enum TrackPolicy {
		/**
		 * Mean that concrete value will be set by {@link ConfigSetBuilder#setTrackPolicy(TrackPolicy)}, and it is {@link TrackPolicy#DISABLE} by default.
		 */
		INHERIT,
		/**
		 * All changes, made in the underlying persistent configuration store, won't be tracked. The values will be loaded and cached only once during program startup.
		 */
		DISABLE,
		/**
		 * <p>All changes, made in the underlying persistent configuration store, will be tracked.
		 * <p>The behavior depends on {@link SourceScheme}:
		 * <ul>
		 * <li>if {@link SourceScheme#CLASSPATH}</li>
		 * Track changes not allowed for this scheme.
		 * <li>if {@link SourceScheme#FILE}</li>
		 * Listen OS notification from file system. Should be supported by operation system.
		 * <li>if {@link SourceScheme#HTTP}</li>
		 * Refresh values from persistent store by schedule with specified period of time.
		 * <li>if {@link SourceScheme#CONFIG_LINKER_SERVER}</li>
		 * Listen notification from remote ConfigLinker server.
		 * </ul>
		 */
		ENABLE,
		;
	}
}
