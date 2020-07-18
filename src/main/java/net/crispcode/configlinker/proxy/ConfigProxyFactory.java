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


package net.crispcode.configlinker.proxy;

import net.crispcode.configlinker.ConfigDescription;
import net.crispcode.configlinker.loaders.LoaderService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Set;


public final class ConfigProxyFactory
{
	private ConfigProxyFactory()
	{
	}
	
	public static Proxy create(HashMap<Class<?>, ConfigDescription> mapConfigDescriptions, LoaderService loaderService)
	{
		ConfigInvocationHandlerImpl configInvocationHandler = new ConfigInvocationHandlerImpl(mapConfigDescriptions, loaderService);
		Set<Class<?>> configInterfaces = mapConfigDescriptions.keySet();
		return (Proxy) Proxy.newProxyInstance(ConfigProxyFactory.class.getClassLoader(),
			configInterfaces.toArray(new Class<?>[configInterfaces.size()]),
			configInvocationHandler);
	}
}
