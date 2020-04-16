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
import net.crispcode.configlinker.annotations.BoundObject;

/**
 * Scheme describe how to access the data source.
 */
public enum SourceScheme
{
	/**
	 * <p>
	 * Mean that concrete value will be get from {@link FactorySettingsBuilder#setSourceScheme(SourceScheme)}, and it is {@link SourceScheme#FILE} by default.
	 */
	INHERIT,
	/**
	 * <p>
	 * Use if the property file resides in the classpath of running VM.<br>
	 * The {@link BoundObject#sourcePath()} should be relative.
	 */
	CLASSPATH,
	/**
	 * <p>
	 * Use if the property file should be loaded from one of mounted file systems.<br>
	 * The {@link BoundObject#sourcePath()} should be absolute or relative.
	 */
	FILE,
	/**
	 * <p>
	 * Use if the {@link BoundObject#sourcePath()} was set as valid URL to load file from HTTP/S server.
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
