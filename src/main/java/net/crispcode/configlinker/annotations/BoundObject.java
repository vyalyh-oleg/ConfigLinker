/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <crispcode.net@gmail.com>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.annotations;

import net.crispcode.configlinker.FactorySettingsBuilder;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.IConfigChangeListener;
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.enums.TrackPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.Charset;


/**
 * You should add this annotation to each interface that is a representation of configuration parameters from `properties` file.
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BoundObject {

	/**
	 * <p>Describes the type of the source that is used to retrieve property values for this annotated object.
	 * <p>Default value is {@link SourceScheme#INHERIT}.
	 * <p>It means that the value will be inherited from {@link FactorySettingsBuilder#setSourceScheme(SourceScheme)} and by default (in builder) equals {@link SourceScheme#FILE}.
	 * @return -
	 */
	SourceScheme sourceScheme() default SourceScheme.INHERIT;

	/**
	 * <p>Must point to source where properties can be found (they will be used to fill in this object with necessary values).
	 * <p>Variables can be used for substituting some parts of this path.
	 * <br>The name of variable can consist of letters, numbers and underscores.
	 * <br>Variables should be set in {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>Path could be relative to the current working directory or to the root of classpath, or it can be an URL (depends on {@link SourceScheme}).
	 * <p>Example path:
	 * <pre>"<b>${substitution1}</b>/path_part1/<b>${substitution2}</b>/path_part2/endPart"</pre>
	 * @return -
	 */
	String sourcePath();
	
	/**
	 * In case when the desired property key couldn't be found in {@link #sourcePath()}, the path from here will be used as a spare to get defaults from.
	 * @return -
	 */
	String defaultSourcePath() default "";
	
	/**
	 * <p>Headers that are used to make http(s) requests to get configuration parameters (if the {@link BoundObject#sourceScheme} is {@link SourceScheme#HTTP}). These values are merged with the values which you can set with {@link FactorySettingsBuilder}.
	 * <p>Variables can be used for substituting some parts of the header.
	 * <br>The name of variable can consist of letters, numbers and underscores.
	 * <br>Variables should be set in {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <p>Example:
	 * <pre>"Auth-Token: <b>${auth_token}</b>"</pre>
	 * @return -
	 */
	String[] httpHeaders() default "";

	/**
	 * <p>This value is used to retrieve {@code Charset} object invoking {@link java.nio.charset.Charset#forName(String)}, and then it will be used to load configuration in raw text format.
	 * <p>The default value is inherited from {@link FactorySettingsBuilder#setCharset(Charset)} and by default (in builder) equals {@code StandardCharsets.UTF_8}.
	 * <p>If you set any value here, it will be used instead of default value.
	 * @return -
	 */
	String charsetName() default "";

	/**
	 * <p>The <b>common part</b> of a group of parameter names in property file. This part is used for construction the full name, which is used for binding methods in annotated interface.<br>
	 * If it is not specified you can use only full parameter names in {@link BoundProperty#name()}.<br>
	 * If it has any value then both variants (full and prefix-aware) can be used.</p>
	 * <br>
	 * <p><u>Example:</u><br>
	 * If the prefix  is set to {@code "mycompany"}, then the {@code @BoundProperty.name()} can be as {@code ".serverName"} (start with dot is obligatory). This means, the final parameter name, which will be searched in property file, is {@code "mycompany.serverName"}.<br>
	 * Without the dot at the beginning, the {@code @BoundProperty.name()} is considered as full parameter name and the prefix is not taken into account.
	 * <p><br>
	 * You can also use variables or method parameters for substituting some parts of the prefix, can be declared as {@code ${globalVariable}}.
	 * <br>The name of variable can consist of letters, numbers and underscores.
	 * <br>Variables should be set with {@link FactorySettingsBuilder#addParameter(String, String)}.
	 * <br>Method parameters should be declared as {@code @{methodParameter}}
	 * <p>
	 * <br>
	 * <u>Example:</u><br>
	 * The names of parameters in file are:
	 * <pre>
	 * "servers.<b>kyiv</b>.<b>test</b>.configuration.ip"
	 * "servers.<b>kyiv</b>.<b>production</b>.configuration.ip"
	 * "servers.<b>lviv</b>.<b>test</b>.configuration.ip"
	 * "servers.<b>lviv</b>.<b>staging</b>.configuration.ip"
	 * </pre>
	 * The code:
	 * <pre>
	 * &#064;BoundObject(sourcePath = "server.properties", propertyNamePrefix = "servers.<b>${affiliate}</b>.<b>@{type}</b>")
	 *   // and for the method:
	 * &#064;BoundProperty(name = ".configuration.ip")
	 * int getIP(String type);
	 *   // in the code:
	 * FactorySettingsBuilder.addParameter("affiliate", "kyiv");
	 *   // or
	 * FactorySettingsBuilder.addParameter("affiliate", "lviv");
	 * </pre>
	 * @return -
	 */
	String propertyNamePrefix() default "";

	/**
	 * <p>Default value is {@link TrackPolicy#INHERIT}
	 * <p>It means that the global policy will be used, which inherited from {@link FactorySettingsBuilder#setTrackPolicy(TrackPolicy)} and by default (in builder) equals {@link TrackPolicy#DISABLE}
	 * @return -
	 */
	TrackPolicy trackingPolicy() default TrackPolicy.INHERIT;

	/**
	 * <p>Used only if in this annotation or in {@link FactorySettingsBuilder#setTrackPolicy(TrackPolicy)} specified {@link TrackPolicy#ENABLE}, and the {@link SourceScheme#HTTP}.
	 * <p>Otherwise this parameter is ignored.
	 * <p>Default value is '0' seconds which means inherited behaviour from {@link FactorySettingsBuilder#setTrackingInterval(int)} and by default (in builder) equals '60' seconds.
	 * <p>MIN value = 15 seconds, MAX value = 86400 seconds (1 day = 24 hours * 3600 seconds).
	 * @return -
	 */
	int trackingInterval() default 0;

	/**
	 * Points to the class which implements the interface {@link IConfigChangeListener}.
	 * @return -
	 */
	Class<? extends IConfigChangeListener> changeListener() default IConfigChangeListener.class;

	/**
	 * <p>What to do if the property value does not exist in underlying persistent store or cannot be converted to object representation for any reasons.
	 * <p>Default value is {@link ErrorBehavior#INHERIT}.
	 * <p>It means that the value will be inherited from {@link FactorySettingsBuilder#setErrorBehavior(ErrorBehavior)} and by default (in builder) equals {@link ErrorBehavior#THROW_EXCEPTION}
	 * @return -
	 */
	ErrorBehavior errorBehavior() default ErrorBehavior.INHERIT;
}
