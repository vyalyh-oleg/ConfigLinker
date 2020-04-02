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

import net.crispcode.configlinker.exceptions.ConfigLinkerRuntimeException;

import java.util.Collections;
import java.util.Map;


/**
 * Event which is passed as an argument in {@link IConfigChangeListener#configChanged(ConfigChangedEvent)}.
 * It contains information about changes were made in the configuration sources.
 */
final public class ConfigChangedEvent
{
	private final Class<?> configInterface;
	private final String sourcePath;
	private final Map<String, ValuesPair> rawValues;
	private final ConfigLinkerRuntimeException exception;
	
	public ConfigChangedEvent(Class<?> configInterface, String sourcePath, Map<String, ValuesPair> rawValues, ConfigLinkerRuntimeException exception)
	{
		this.configInterface = configInterface;
		this.sourcePath = sourcePath;
		this.rawValues = Collections.unmodifiableMap(rawValues);
		this.exception = exception;
	}
	
	public Class<?> getConfigInterface()
	{
		return configInterface;
	}
	
	public String getSourcePath()
	{
		return sourcePath;
	}
	
	/**
	 * @return Keys and values (old, new) that were changed. Can be null (if the error happened before new properties have been loaded).
	 */
	public Map<String, ValuesPair> getRawValues()
	{
		return rawValues;
	}
	
	/**
	 * @return The exception with the reason if an error occurred during configuration update.<br>
	 * In such case {@link #getRawValues()} can return null (if the error had happened before new properties was loaded).
	 */
	public ConfigLinkerRuntimeException getException()
	{
		return exception;
	}
	
	public static class ValuesPair
	{
		private String oldValue;
		private String newValue;
		
		public ValuesPair(String oldValue, String newValue)
		{
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
		public String getOldValue()
		{
			return oldValue;
		}
		
		public String getNewValue()
		{
			return newValue;
		}
	}
}
