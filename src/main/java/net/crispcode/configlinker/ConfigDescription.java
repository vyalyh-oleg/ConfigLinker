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


package net.crispcode.configlinker;

import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.enums.TrackPolicy;
import net.crispcode.configlinker.mappers.IPropertyMapper;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;


public final class ConfigDescription
{
	private final Class<?> confInterface;
	private SourceScheme sourceScheme;
	private Map<String, String> httpHeaders;
	private String sourcePath;
	private String defaultSourcePath;
	private Charset charset;
	private boolean ignoreWhitespaces;
	private String propertyNamePrefix;
	private TrackPolicy trackPolicy;
	private int trackingInterval;
	private IConfigChangeListener configChangeListener;
	private ErrorBehavior errorBehavior;
	private Map<Method, PropertyDescription> boundPropertyMethods;
	
	ConfigDescription(Class<?> confInterface)
	{
		this.confInterface = confInterface;
	}
	
	
	public Class<?> getConfInterface()
	{
		return confInterface;
	}
	
	public SourceScheme getSourceScheme()
	{
		return sourceScheme;
	}
	
	public Map<String, String> getHttpHeaders()
	{
		return httpHeaders;
	}
	
	public String getSourcePath()
	{
		return sourcePath;
	}
	
	public String getDefaultSourcePath()
	{
		return defaultSourcePath;
	}
	
	public Charset getCharset()
	{
		return charset;
	}
	
	public boolean isIgnoreWhitespaces()
	{
		return ignoreWhitespaces;
	}
	
	public String getPropertyNamePrefix()
	{
		return propertyNamePrefix;
	}
	
	public TrackPolicy getTrackPolicy()
	{
		return trackPolicy;
	}
	
	/**
	 * @return Interval in seconds, at which refreshing of configuration values occurs.
	 */
	public int getTrackingInterval()
	{
		return trackingInterval;
	}
	
	/**
	 * @return Object that implements of the same name interface.
	 */
	public IConfigChangeListener getConfigChangeListener()
	{
		return configChangeListener;
	}
	
	public ErrorBehavior getErrorBehavior()
	{
		return errorBehavior;
	}
	
	public Map<Method, PropertyDescription> getBoundPropertyMethods()
	{
		return boundPropertyMethods;
	}
	
	
	void setSourceScheme(SourceScheme sourceScheme)
	{
		this.sourceScheme = sourceScheme;
	}
	
	void setHttpHeaders(Map<String, String> httpHeaders)
	{
		this.httpHeaders = httpHeaders;
	}
	
	void setSourcePath(String sourcePath)
	{
		this.sourcePath = sourcePath;
	}
	
	void setDefaultSourcePath(String defaultSourcePath)
	{
		this.defaultSourcePath = defaultSourcePath;
	}
	
	void setCharset(Charset charset)
	{
		this.charset = charset;
	}
	
	void setIgnoreWhitespaces(boolean ignoreWhitespaces)
	{
		this.ignoreWhitespaces = ignoreWhitespaces;
	}
	
	void setPropertyNamePrefix(String propertyNamePrefix)
	{
		this.propertyNamePrefix = propertyNamePrefix;
	}
	
	void setTrackPolicy(TrackPolicy trackPolicy)
	{
		this.trackPolicy = trackPolicy;
	}
	
	void setTrackingInterval(int trackingInterval)
	{
		this.trackingInterval = trackingInterval;
	}
	
	void setConfigChangeListener(IConfigChangeListener configChangeListener)
	{
		this.configChangeListener = configChangeListener;
	}
	
	void setErrorBehavior(ErrorBehavior errorBehavior)
	{
		this.errorBehavior = errorBehavior;
	}
	
	void setBoundPropertyMethods(Map<Method, PropertyDescription> boundPropertyMethods)
	{
		this.boundPropertyMethods = boundPropertyMethods;
	}
	
	
	public void fireConfigChanged(ConfigChangedEvent configChangedEvent)
	{
		if (configChangeListener != null)
			configChangeListener.configChanged(configChangedEvent);
	}
	
	
	public final class PropertyDescription
	{
		private final String name;
		private final String[] dynamicVariableNames; // can be null
		private final IPropertyMapper mapper;
		private final ErrorBehavior errorBehavior;
		
		public PropertyDescription(String name, String[] dynamicVariableNames, IPropertyMapper propertyMapper, ErrorBehavior errorBehavior)
		{
			ConfigDescription configDescription = ConfigDescription.this;
			this.name = name;
			this.dynamicVariableNames = dynamicVariableNames;
			this.errorBehavior = errorBehavior;
			this.mapper = propertyMapper;
		}
		
		public ConfigDescription getConfigDescription()
		{
			return ConfigDescription.this;
		}
		
		/**
		 * @return Full property name, if method don't have parameters, or name template (contains @{paramName} construction) in other case.
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * @return Variable names in configuration method or {@code null} if the method has no argument.
		 */
		public String[] getDynamicVariableNames()
		{
			return dynamicVariableNames;
		}
		
		public IPropertyMapper getMapper()
		{
			return mapper;
		}
		
		public ErrorBehavior getErrorBehavior()
		{
			return errorBehavior;
		}
	}
}
