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
import net.crispcode.configlinker.exceptions.ConfigProxyException;
import net.crispcode.configlinker.loaders.LoaderService;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;


final class ConfigInvocationHandlerImpl implements InvocationHandler
{
	static private final Constructor<MethodHandles.Lookup> constructor;
	
	static
	{
		try
		{
			constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
			constructor.setAccessible(true);
		}
		catch (NoSuchMethodException e)
		{
			throw new ConfigProxyException(e).logAndReturn();
		}
	}
	
	private final HashMap<Class<?>, ConfigDescription> mapConfigDescriptions;
	private final LoaderService loaderService;
	
	ConfigInvocationHandlerImpl(HashMap<Class<?>, ConfigDescription> mapConfigDescriptions, LoaderService loaderService)
	{
		this.mapConfigDescriptions = mapConfigDescriptions;
		this.loaderService = loaderService;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		final Class<?> declaringClass = method.getDeclaringClass();
		if (method.getName().equals("toString"))
			return "['ConfigLinker' library] Proxy for configuration interface '" + declaringClass.getName() + "'.";
		
		final ConfigDescription configDescription = mapConfigDescriptions.get(declaringClass);
		if (configDescription != null)
		{
			ConfigDescription.PropertyDescription propertyDescription = configDescription.getBoundPropertyMethods().get(method);
			
			if (propertyDescription != null)
				return loaderService.getProperty(propertyDescription, method, args);
		}
		
		if (method.isDefault())
		{
			return constructor.newInstance(declaringClass)
				.in(declaringClass)
				.unreflectSpecial(method, declaringClass)
				.bindTo(proxy)
				.invokeWithArguments(args);
		}
		
		return null;
	}
}
