package net.crispcode.configlinker;

import net.crispcode.configlinker.annotations.BoundObject;

/**
 * Implement this interface and point your class in annotation {@link BoundObject#changeListener()} for receiving notifications on configuration updates.
 */
public interface IConfigChangeListener
{
	void configChanged(ConfigChangedEvent configChangedEvent);
}
