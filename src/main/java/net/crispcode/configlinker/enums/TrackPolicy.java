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
	 * <li>if {@link SourceScheme#FILE} -- listen OS notification from the file system. Should be supported by operation system.</li>
	 * <li>if {@link SourceScheme#HTTP} -- refresh values from the remote store by schedule with specified period of time.</li>
	 * <li>if {@link SourceScheme#CONFIG_LINKER_SERVER} -- listen notification from remote ConfigLinker server (not implemented yet).</li>
	 * </ul>
	 */
	ENABLE,
	;
}
