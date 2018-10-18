package com.configlinker.annotations;

import com.configlinker.ConfigChangeListener;
import com.configlinker.ErrorBehavior;
import com.configlinker.FactoryConfigBuilder;

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
	 * <p>
	 * Must point to source where data can be found to fill in this object with necessary values.
	 * <p>
	 * You can use variables for substituting some parts of this path.
	 * Variables can be set in {@link FactoryConfigBuilder#addParameter(String, String)}.
	 * <p>
	 * Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endPart"</pre>
	 * @return -
	 */
	String sourcePath();

	/**
	 * <p>
	 * Headers that are used to make requests to get configuration parameters (if the {@link BoundObject#sourceScheme} is {@link SourceScheme#HTTP}). These values are merged with values which you can set with {@link FactoryConfigBuilder}.
	 * <p>
	 * You can use variables for substituting some parts of this path.
	 * Variables can be set in {@link FactoryConfigBuilder#addParameter(String, String)}.
	 * <p>
	 * Example path:
	 * <pre>"${substitution1}/path_part1/${substitution2}/path_part2/endName"</pre>
	 * @return -
	 */
	String[] httpHeaders() default "";

	/**
	 * <p>This value is used to retrieve {@code Charset} object invoking {@link java.nio.charset.Charset#forName(String)}, and then it will be used to load configuration in raw text format.
	 * <p>Default value inherited from {@link FactoryConfigBuilder#setCharset(Charset)} and equal {@code StandardCharsets.UTF_8}.
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
	 * Variables should be set with {@link FactoryConfigBuilder#addParameter(String, String)}.
	 * <p>
	 * Example:
	 * <pre>	"servers.${type}.srv1.configuration"</pre>
	 * Where the {@code type} can be for example "test" or "production", etc.:
	 * <pre>	"servers.<b>test</b>.srv1.configuration"</pre>
	 * @return -
	 */
	String propertyNamePrefix() default "";

	/**
	 * <p>Default value is {@link TrackPolicy#INHERIT} which mean global policy will be used (specify in method {@link FactoryConfigBuilder#setTrackPolicy(BoundObject.TrackPolicy)}). To override such behaviour choose {@link TrackPolicy#DISABLE} or {@link TrackPolicy#ENABLE} value.
	 * @return -
	 */
	TrackPolicy trackPolicy() default TrackPolicy.INHERIT;

	/**
	 * <p>Used only if here or in {@link FactoryConfigBuilder#setTrackPolicy(BoundObject.TrackPolicy)} specified {@link TrackPolicy#ENABLE}, and the {@link SourceScheme#HTTP}.
	 * <p>Otherwise this parameter ignored.
	 * <p>Default value is '0' which mean inherited behaviour (will be used value from {@code FactoryConfigBuilder} (equal '60' seconds).
	 * <p>MIN value = 15 seconds, MAX value = 86400 seconds (1 day = 24 hours * 3600 seconds).
	 * @return -
	 */
	int trackingInterval() default 0;

	/**
	 *
	 * @return -
	 */
	Class<? extends ConfigChangeListener> changeListener() default ConfigChangeListener.class;

	/**
	 * <p>
	 * What to do if the property value does not exist in underlying persistent store.
	 * Default value is {@link ErrorBehavior#INHERITED} and specified in {@link FactoryConfigBuilder#setErrorBehavior(ErrorBehavior)}
	 * @return -
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERITED;

	/**
	 * Scheme describe how to access the data source.
	 */
	enum SourceScheme {
		/**
		 * <p>
		 * Mean that concrete value will be get from {@link FactoryConfigBuilder#setSourceScheme(BoundObject.SourceScheme)}, and it is {@link SourceScheme#FILE} by default.
		 */
		INHERIT,
		/**
		 * <p>
		 * Use when you set relative {@link BoundObject#sourcePath()} as file valid system path to load file that resides in classpath of running VM.
		 */
		CLASSPATH,
		/**
		 * <p>
		 * Use when you set absolute or relative {@link BoundObject#sourcePath()} as valid file system path to load file from one of mounted file systems.
		 */
		FILE,
		/**
		 * <p>
		 * Use when you set {@link BoundObject#sourcePath()} as valid URL to load file from HTTP/S server.
		 */
		HTTP,
		/**
		 * <p>
		 * Choose when you are using special ConfigLinker server for configurations management of groups of server and/or services.<br>
		 * <b>It is not implemented yet.</b>
		 */
		CONFIG_LINKER_SERVER
		;
	}

	/**
	 * Policy for refresh configuration parameters.
	 */
	enum TrackPolicy {
		/**
		 * <p>
		 * Mean that concrete value will be set by {@link FactoryConfigBuilder#setTrackPolicy(BoundObject.TrackPolicy)}, and it is {@link TrackPolicy#DISABLE} by default.
		 */
		INHERIT,
		/**
		 * <p>
		 * All changes, made in the underlying persistent configuration store, won't be tracked. The values will be loaded and cached only once during program startup.
		 */
		DISABLE,
		/**
		 * <p>All changes, made in the underlying persistent configuration store, will be tracked.
		 * <p>The behavior depends on {@link SourceScheme}:
		 * <ul>
		 * <li>if {@link SourceScheme#CLASSPATH} -- track changes not allowed for this scheme.</li>
		 * <li>if {@link SourceScheme#FILE} -- listen OS notification from file system. Should be supported by operation system.</li>
		 * <li>if {@link SourceScheme#HTTP} -- refresh values from persistent store by schedule with specified period of time.</li>
		 * <li>if {@link SourceScheme#CONFIG_LINKER_SERVER} -- listen notification from remote ConfigLinker server.</li>
		 * </ul>
		 */
		ENABLE,
		;
	}
}
