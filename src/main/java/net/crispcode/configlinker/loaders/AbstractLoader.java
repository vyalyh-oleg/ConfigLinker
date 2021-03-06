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


package net.crispcode.configlinker.loaders;

import net.crispcode.configlinker.ConfigChangedEvent;
import net.crispcode.configlinker.ConfigDescription;
import net.crispcode.configlinker.IConfigChangeListener;
import net.crispcode.configlinker.Loggers;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.exceptions.ConfigLinkerRuntimeException;
import net.crispcode.configlinker.exceptions.ConfigProxyException;
import net.crispcode.configlinker.exceptions.PropertyLoadException;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.exceptions.PropertyMatchException;
import net.crispcode.configlinker.exceptions.PropertyNotFoundException;
import net.crispcode.configlinker.exceptions.PropertyValidateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


abstract class AbstractLoader
{
	private final Map<Class<?>, ConfigDescription> configDescriptions;
	private final HashMap<String, Properties> rawProperties = new HashMap<>();
	private final HashMap<String, HashMap<String, String>> rawDefaultProperties = new HashMap<>();
	private final HashMap<Class<?>, HashMap<Integer, Object>> singleReturnsMethodsCache = new HashMap<>();
	private final HashMap<Class<?>, HashMap<Integer, Object>> multiReturnsMethodsCache = new HashMap<>();
	
	AbstractLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException
	{
		this.configDescriptions = Collections.unmodifiableMap(configDescriptions);
		this.loadDefaults();
	}
	
	abstract protected void prepareLoader() throws PropertyLoadException;
	
	abstract protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException;
	
	private void loadDefaults()
	{
		for (ConfigDescription configDescription : this.configDescriptions.values())
		{
			HashMap<String, String> propsMap;
			URL defaultsURL;
			
			if (configDescription.getDefaultSourcePath() != null)
			{
				defaultsURL = configDescription.getConfInterface().getResource(configDescription.getDefaultSourcePath());
				
				if (defaultsURL == null)
				{
					throw new PropertyLoadException(
						"Configuration file for defaultSourcePath '" + configDescription.getDefaultSourcePath()
							+ "' doesn't exist; see annotation parameter @BoundObject.defaultSourcePath() on interface '"
							+ configDescription.getConfInterface().getName() + "'.")
						.logAndReturn();
				}
				
				if (this.rawDefaultProperties.containsKey(defaultsURL.toString()))
					continue;
				
				propsMap = getDefaultProperties(defaultsURL, configDescription.getConfInterface().getName(), configDescription.getCharset());
				this.rawDefaultProperties.put(defaultsURL.toString(), propsMap);
			}
		}
	}
	
	private HashMap<String, String> getDefaultProperties(URL defaultsURL, String configDescriptionInterfaceName, Charset charset)
	{
		Properties newProperties;
		try (BufferedReader propFileReader = new BufferedReader(new InputStreamReader(defaultsURL.openStream(), charset), 256 * 1024))
		{
			newProperties = new Properties();
			newProperties.load(propFileReader);
		}
		catch (IOException e)
		{
			throw new PropertyLoadException(
				"Error during loading default properties from file '" + defaultsURL + "' with charset '" + charset + "', config interface: '"
					+ configDescriptionInterfaceName + "'.", e)
				.logAndReturn();
		}
		
		HashMap<String, String> propsMap = newProperties.entrySet().stream().collect(
			HashMap::new,
			(map, propEntry) -> map.put((String) propEntry.getKey(), (String) propEntry.getValue()),
			HashMap::putAll);
		
		return propsMap;
	}
	
	protected void startTrackChanges() throws PropertyLoadException
	{
		throw new PropertyLoadException(
			"Cannot perform 'startTrackChanges', because tracking changes didn't implemented for '" + this.getClass().getName() + "'.")
			.logAndReturn();
	}
	
	protected void stopTrackChanges() throws PropertyLoadException
	{
		throw new PropertyLoadException(
			"Cannot perform 'stopTrackChanges', because tracking changes didn't implemented for '" + this.getClass().getName() + "'.")
			.logAndReturn();
	}
	
	final Collection<ConfigDescription> getConfigDescriptions()
	{
		return this.configDescriptions.values();
	}
	
	final void loadProperties() throws PropertyLoadException, PropertyValidateException, PropertyMatchException, PropertyMapException
	{
		for (ConfigDescription configDescription : this.configDescriptions.values())
		{
			Properties newProperties = this.rawProperties
				.computeIfAbsent(configDescription.getSourcePath(), (sourcePath) -> this.loadRawProperties(configDescription));
			HashMap<Integer, Object> singleReturns = this.convertSingleRawPropertiesToObjects(configDescription, newProperties);
			
			this.singleReturnsMethodsCache.put(configDescription.getConfInterface(), singleReturns);
			this.multiReturnsMethodsCache.put(configDescription.getConfInterface(), new HashMap<>());
		}
	}
	
	private HashMap<Integer, Object> convertSingleRawPropertiesToObjects(ConfigDescription configDescription, Properties newProperties)
		throws PropertyLoadException, PropertyMatchException, PropertyValidateException, PropertyMapException
	{
		Class<?> configInterface = configDescription.getConfInterface();
		HashMap<Integer, Object> singleReturns = new HashMap<>();
		
		HashMap<String, String> defaults = null;
		if (configDescription.getDefaultSourcePath() != null)
		{
			URL defaultsURL = configDescription.getConfInterface().getResource(configDescription.getDefaultSourcePath());
			defaults = this.rawDefaultProperties.get(defaultsURL.toString());
		}
		
		for (Map.Entry<Method, ConfigDescription.PropertyDescription> entryPropertyDescription : configDescription.getBoundPropertyMethods().entrySet())
		{
			ConfigDescription.PropertyDescription propertyDescription = entryPropertyDescription.getValue();
			
			if (propertyDescription.getDynamicVariableNames() != null)
				continue;
			
			ErrorBehavior errorBehavior = propertyDescription.getErrorBehavior();
			Method propertyMethod = entryPropertyDescription.getKey();
			String fullPropertyName = propertyDescription.getName();
			String rawValue = newProperties.getProperty(fullPropertyName);
			
			Object objValue = null;
			if (rawValue == null || rawValue.isEmpty())
			{
				if (errorBehavior == ErrorBehavior.THROW_EXCEPTION)
				{
					throw new PropertyNotFoundException(
						"Value for property '" + fullPropertyName + "' not found, config interface '" + configInterface.getName() + "#"
							+ propertyMethod.getName() + "'.")
						.logAndReturn();
				}
				
				if (errorBehavior == ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION || errorBehavior == ErrorBehavior.TRY_DEFAULTS_OR_NULL)
				{
					rawValue = defaults.get(fullPropertyName);
					if ((rawValue == null || rawValue.isEmpty()) && errorBehavior == ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
						throw new PropertyNotFoundException(
							"Default value for property '" + fullPropertyName + "' not found, config interface '" + configInterface.getName() + "#"
								+ propertyMethod.getName() + "'.")
							.logAndReturn();
				}
			}
			
			if (rawValue != null && !rawValue.isEmpty())
				objValue = propertyDescription.getMapper().mapFromString(rawValue);
			
			singleReturns.put(computeKeyHash(fullPropertyName, propertyMethod), objValue);
		}
		
		return singleReturns;
	}
	
	/**
	 * <p>Suppress all exceptions, because this methods is called during runtime when configuration is refreshed. If exception appear during refreshing ConfigLinker just send {@link ConfigChangedEvent} to all {@link IConfigChangeListener}s.
	 *
	 * @param configDescriptions -
	 */
	final void refreshProperties(Set<ConfigDescription> configDescriptions)
	{
		ConfigDescription description = configDescriptions.iterator().next();
		Properties oldProperties = rawProperties.get(description.getSourcePath());
		Properties newProperties;
		try
		{
			newProperties = this.loadRawProperties(description);
		}
		catch (ConfigLinkerRuntimeException e)
		{
			Loggers.getMainLogger().error("Cannot load raw properties for config interface '{}'.", description.getConfInterface().getName());
			for (ConfigDescription configDescription : configDescriptions)
			{
				ConfigChangedEvent configChangedEvent = new ConfigChangedEvent(configDescription.getConfInterface(), configDescription.getSourcePath(),
					null, e);
				configDescription.fireConfigChanged(configChangedEvent);
			}
			return;
		}
		
		
		HashMap<String, ConfigChangedEvent.ValuesPair> changedValues = new HashMap<>();
		for (Map.Entry<Object, Object> oldEntry : oldProperties.entrySet())
		{
			String oldKey = (String) oldEntry.getKey();
			String oldValue = (String) oldEntry.getValue();
			String newValue = newProperties.getProperty(oldKey);
			
			if ((oldValue != null && !oldValue.equals(newValue)) || (oldValue == null && newValue != null))
				changedValues.put(oldKey, new ConfigChangedEvent.ValuesPair(oldValue, newValue));
		}
		
		for (Map.Entry<Object, Object> newEntry : newProperties.entrySet())
		{
			if (!oldProperties.containsKey(newEntry.getKey()))
				changedValues.put((String) newEntry.getKey(), new ConfigChangedEvent.ValuesPair(null, (String) newEntry.getValue()));
		}
		
		
		HashMap<Class<?>, HashMap<Integer, Object>> singleReturnsMethodsCache = new HashMap<>();
		HashMap<ConfigDescription, ConfigChangedEvent> configChangedEvents = new HashMap<>();
		boolean error = false;
		
		for (ConfigDescription configDescription : configDescriptions)
		{
			Class<?> configInterface = configDescription.getConfInterface();
			
			HashMap<Integer, Object> singleReturns = null;
			ConfigLinkerRuntimeException convertException = null;
			try
			{
				singleReturns = this.convertSingleRawPropertiesToObjects(configDescription, newProperties);
			}
			catch (ConfigLinkerRuntimeException e)
			{
				Loggers.getMainLogger().error("Cannot convert raw properties to objects for config interface '{}'.", configInterface.getName(), e);
				convertException = e;
				error = true;
			}
			
			ConfigChangedEvent configChangedEvent = new ConfigChangedEvent(configInterface, configDescription.getSourcePath(), changedValues, convertException);
			configChangedEvents.put(configDescription, configChangedEvent);
			singleReturnsMethodsCache.put(configInterface, singleReturns);
		}
		
		if (!error)
		{
			this.rawProperties.put(description.getSourcePath(), newProperties);
			this.singleReturnsMethodsCache.putAll(singleReturnsMethodsCache);
			configDescriptions.forEach(confDescr -> this.multiReturnsMethodsCache.get(confDescr.getConfInterface()).clear());
		}
		
		configDescriptions.forEach(confDescr -> confDescr.fireConfigChanged(configChangedEvents.get(confDescr)));
	}
	
	final Object getProperty(ConfigDescription.PropertyDescription propertyDescription, Method method, HashMap<String, String> methodArguments)
		throws ConfigProxyException, PropertyValidateException, PropertyMatchException, PropertyLoadException
	{
		final ConfigDescription configDescription = propertyDescription.getConfigDescription();
		Class<?> configInterface = configDescription.getConfInterface();
		
		if (propertyDescription.getDynamicVariableNames() == null)
			return this.singleReturnsMethodsCache.get(configInterface).get(computeKeyHash(propertyDescription.getName(), method));
		
		String fullPropertyName = validateAndMakeVariableSubstitution(propertyDescription.getName(), methodArguments);
		
		Object objValue = this.multiReturnsMethodsCache.get(configInterface).computeIfAbsent(
			computeKeyHash(fullPropertyName, method),
			key -> {
				Object value = null;
				
				String rawValue = this.rawProperties.get(configDescription.getSourcePath()).getProperty(fullPropertyName);
				if (rawValue == null || rawValue.isEmpty())
				{
					if (propertyDescription.getErrorBehavior() == ErrorBehavior.THROW_EXCEPTION)
					{
						throw new PropertyNotFoundException(
							"Value for property '" + propertyDescription.getName() + "' not found, config interface '" + configInterface.getName()
								+ "#" + method.getName() + "'.")
							.logAndReturn();
					}
				}
				else
				{
					value = propertyDescription.getMapper().mapFromString(rawValue);
				}
				
				return value;
			});
		
		return objValue;
	}
	
	private String validateAndMakeVariableSubstitution(String parameterNamePattern, HashMap<String, String> methodArguments) throws ConfigProxyException
	{
		for (Map.Entry<String, String> entryArgument : methodArguments.entrySet())
		{
			if (entryArgument.getValue() == null)
				throw new ConfigProxyException("Not found value for variable '" + entryArgument.getKey() + "'.").logAndReturn();
			
			parameterNamePattern = parameterNamePattern.replaceAll("@\\{" + entryArgument.getKey() + "\\}", entryArgument.getValue());
		}
		return parameterNamePattern;
	}
	
	private int computeKeyHash(String propertyName, Method configMethod)
	{
		int hash = propertyName.hashCode();
		return hash + 31 * (configMethod.hashCode() ^ hash);
	}
}
