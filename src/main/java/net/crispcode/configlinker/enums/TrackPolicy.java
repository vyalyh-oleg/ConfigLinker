package net.crispcode.configlinker.enums;

import net.crispcode.configlinker.FactorySettingsBuilder;

/**
 * Policy for refresh configuration parameters.
 */
public enum TrackPolicy
{
	/**
	 * <p>
	 * Mean that concrete value will be set by {@link FactorySettingsBuilder#setTrackPolicy(TrackPolicy)}, and it is {@link TrackPolicy#DISABLE} by default.
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
