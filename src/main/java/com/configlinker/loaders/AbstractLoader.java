package com.configlinker.loaders;

import com.configlinker.ConfigChangedEvent;
import com.configlinker.ConfigDescription;
import com.configlinker.IConfigChangeListener;
import com.configlinker.Loggers;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.exceptions.ConfigLinkerRuntimeException;
import com.configlinker.exceptions.ConfigProxyException;
import com.configlinker.exceptions.PropertyLoadException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.exceptions.PropertyMatchException;
import com.configlinker.exceptions.PropertyNotFoundException;
import com.configlinker.exceptions.PropertyValidateException;

import java.lang.reflect.Method;
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
	private final HashMap<Class<?>, HashMap<Integer, Object>> singleReturnsMethodsCache = new HashMap<>();
	private final HashMap<Class<?>, HashMap<Integer, Object>> multiReturnsMethodsCache = new HashMap<>();
	
	AbstractLoader(HashMap<Class<?>, ConfigDescription> configDescriptions) throws PropertyLoadException
	{
		this.configDescriptions = Collections.unmodifiableMap(configDescriptions);
	}
	
	abstract protected void prepareLoader() throws PropertyLoadException;
	
	abstract protected Properties loadRawProperties(ConfigDescription configDescription) throws PropertyLoadException;
	
	protected void startTrackChanges() throws PropertyLoadException
	{
		throw new PropertyLoadException(
			"Cannot perform 'startTrackChanges', because tracking changes didn't implemented for '" + this.getClass().getName() + "'.").logAndReturn();
	}
	
	protected void stopTrackChanges() throws PropertyLoadException
	{
		throw new PropertyLoadException(
			"Cannot perform 'stopTrackChanges', because tracking changes didn't implemented for '" + this.getClass().getName() + "'.").logAndReturn();
	}
	
	final protected Collection<ConfigDescription> getConfigDescriptions()
	{
		return this.configDescriptions.values();
	}
	
	final protected void loadProperties() throws PropertyLoadException, PropertyValidateException, PropertyMatchException, PropertyMapException
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
		
		for (Map.Entry<Method, ConfigDescription.PropertyDescription> entryPropertyDescription : configDescription.getBoundPropertyMethods().entrySet())
		{
			ConfigDescription.PropertyDescription propertyDescription = entryPropertyDescription.getValue();
			
			if (propertyDescription.getDynamicVariableNames() != null)
				continue;
			
			String fullPropertyName = propertyDescription.getName();
			String rawValue = newProperties.getProperty(fullPropertyName);
			
			Object objValue = null;
			if (rawValue == null || rawValue.isEmpty())
			{
				if (propertyDescription.getErrorBehavior() == ErrorBehavior.THROW_EXCEPTION)
				{
					throw new PropertyNotFoundException(
						"Value for property '" + fullPropertyName + "' not found, config interface '" + configInterface.getName() +
							"', method '" + entryPropertyDescription.getKey().getName() + "'.").logAndReturn();
				}
			}
			else
			{
				objValue = propertyDescription.getMapper().mapFromString(rawValue);
			}
			
			singleReturns.put(computeKeyHash(fullPropertyName, entryPropertyDescription.getKey()), objValue);
		}
		
		return singleReturns;
	}
	
	/**
	 * <p>Suppress all exceptions, because this methods is called during runtime when configuration is refreshed. If exception appear during refreshing ConfigLinker just send {@link ConfigChangedEvent} to all {@link IConfigChangeListener}s.
	 *
	 * @param configDescriptions -
	 */
	final protected void refreshProperties(Set<ConfigDescription> configDescriptions)
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
				ConfigChangedEvent configChangedEvent = new ConfigChangedEvent(configDescription.getConfInterface(), configDescription.getSourcePath(), null,
					e);
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
			if (!oldProperties.contains(newEntry.getKey()))
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
			singleReturnsMethodsCache.put(configDescription.getConfInterface(), singleReturns);
		}
		
		if (!error)
		{
			this.rawProperties.put(description.getSourcePath(), newProperties);
			this.singleReturnsMethodsCache.putAll(singleReturnsMethodsCache);
			configDescriptions.forEach(confDescr -> this.multiReturnsMethodsCache.get(confDescr.getConfInterface()).clear());
		}
		
		configDescriptions.forEach(confDescr -> confDescr.fireConfigChanged(configChangedEvents.get(confDescr)));
	}
	
	final Object getProperty(ConfigDescription configDescription, ConfigDescription.PropertyDescription propertyDescription, Method method,
		HashMap<String, String> methodArguments)
		throws ConfigProxyException, PropertyValidateException, PropertyMatchException, PropertyLoadException
	{
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
							"Value for property '" + propertyDescription.getName() + "' not found, config interface '" + configInterface.getName() +
								"', method '" + method.getName() + "'.").logAndReturn();
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
