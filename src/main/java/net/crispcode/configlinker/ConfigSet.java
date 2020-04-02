/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

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


package net.crispcode.configlinker;

import net.crispcode.configlinker.exceptions.ConfigSetException;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Is the class which object you will use throughout you code for retrieving groups of configuration parameters. In other words <u>it contains a set of config groups</u>. In our case such a group of configuration parameters <u>is just an interface which is <b>bound</b> to a properties file</u>
 */
public final class ConfigSet
{
	private final Proxy configProxy;
	private final List<Class<?>> configInterfaces;
	private final Runnable stopTrackChanges;
	
	ConfigSet(Proxy configProxy, Runnable stopTrackChanges)
	{
		this.configProxy = configProxy;
		this.configInterfaces = Collections.unmodifiableList(Arrays.asList(this.configProxy.getClass().getInterfaces()));
		this.stopTrackChanges = stopTrackChanges;
	}
	
	/**
	 * @return All interfaces which is served by this {@link ConfigSet} instance.
	 */
	public List<Class<?>> getAllConfigInterfaces()
	{
		return configInterfaces;
	}
	
	/**
	 * @param configInterface Interface annotated with {@link net.crispcode.configlinker.annotations.BoundObject} and which methods (at least one) annotated with {@link net.crispcode.configlinker.annotations.BoundProperty} and which was previously passed as an argument to one of the {@link ConfigSetFactory} methods.
	 * @param <T> particular type of the {@code configInterface}, it is retrieved automatically from the interface {@code Class} object.
	 * @return A proxy object which implement interface {@link T}, and which will be used for getting specific configuration values from bound property file.
	 * @throws ConfigSetException
	 */
	public <T> T getConfigObject(Class<T> configInterface) throws ConfigSetException
	{
		if (configInterfaces.contains(configInterface))
			return configInterface.cast(this.configProxy);
		throw new ConfigSetException("This ConfigSet doesn't contain configuration for '" + configInterface.getName() + "'.");
	}
	
	public void stopTrackChanges()
	{
		stopTrackChanges.run();
	}
}
