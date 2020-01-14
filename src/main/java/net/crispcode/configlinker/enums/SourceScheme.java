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
