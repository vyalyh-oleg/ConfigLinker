package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.mappers.PropertyMapper;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;


public final class ConfigDescription {
	private final Class<?> confInterface;
	private BoundObject.SourceScheme sourceScheme;
	private Map<String, String> httpHeaders;
	private String sourcePath;
	private Charset charset;
	private boolean ignoreWhitespaces;
	private String propertyNamePrefix;
	private BoundObject.TrackPolicy trackPolicy;
	private int trackingInterval;
	private ConfigChangeListener configChangeListener;
	private ErrorBehavior errorBehavior;
	private Map<Method, PropertyDescription> boundPropertyMethods;

	ConfigDescription(Class<?> confInterface) {
		this.confInterface = confInterface;
	}


	public Class<?> getConfInterface() {
		return confInterface;
	}

	public BoundObject.SourceScheme getSourceScheme() {
		return sourceScheme;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public Charset getCharset() {
		return charset;
	}

	public boolean isIgnoreWhitespaces() {
		return ignoreWhitespaces;
	}

	public String getPropertyNamePrefix() {
		return propertyNamePrefix;
	}

	public BoundObject.TrackPolicy getTrackPolicy() {
		return trackPolicy;
	}

	/**
	 *
	 * @return Interval in seconds, through which refreshing of configuration values occurs.
	 */
	public int getTrackingInterval() {
		return trackingInterval;
	}

	/**
	 *
	 * @return Object that implements of the same name interface.
	 */
	public ConfigChangeListener getConfigChangeListener() {
		return configChangeListener;
	}

	public ErrorBehavior getErrorBehavior() {
		return errorBehavior;
	}

	public Map<Method, PropertyDescription> getBoundPropertyMethods() {
		return boundPropertyMethods;
	}


	void setSourceScheme(BoundObject.SourceScheme sourceScheme) {
		this.sourceScheme = sourceScheme;
	}

	void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	void setCharset(Charset charset) {
		this.charset = charset;
	}

	void setIgnoreWhitespaces(boolean ignoreWhitespaces) {
		this.ignoreWhitespaces = ignoreWhitespaces;
	}

	void setPropertyNamePrefix(String propertyNamePrefix) {
		this.propertyNamePrefix = propertyNamePrefix;
	}

	void setTrackPolicy(BoundObject.TrackPolicy trackPolicy) {
		this.trackPolicy = trackPolicy;
	}

	void setTrackingInterval(int trackingInterval) {
		this.trackingInterval = trackingInterval;
	}

	void setConfigChangeListener(ConfigChangeListener configChangeListener) {
		this.configChangeListener = configChangeListener;
	}

	void setErrorBehavior(ErrorBehavior errorBehavior) {
		this.errorBehavior = errorBehavior;
	}

	void setBoundPropertyMethods(Map<Method, PropertyDescription> boundPropertyMethods) {
		this.boundPropertyMethods = boundPropertyMethods;
	}


	public void fireConfigChanged(ConfigChangedEvent configChangedEvent) {
		if (configChangeListener != null)
			configChangeListener.configChanged(configChangedEvent);
	}


	public final class PropertyDescription {
		private final String name;
		private final String[] dynamicVariableNames; // can be null
		private final PropertyMapper mapper;
		private final ErrorBehavior errorBehavior;

		public PropertyDescription(String name, String[] dynamicVariableNames, PropertyMapper propertyMapper, ErrorBehavior errorBehavior) {
			this.name = name;
			this.dynamicVariableNames = dynamicVariableNames;
			this.errorBehavior = errorBehavior;
			this.mapper = propertyMapper;
		}

		/**
		 * @return Full property name, if method don't have parameters, or name template (contains @{paramName} construction) in other case.
		 */
		public String getName() {
			return name;
		}

		/**
		 *
		 * @return Variable names in configuration method or {@code null} if the method has no argument.
		 */
		public String[] getDynamicVariableNames() {
			return dynamicVariableNames;
		}

		public PropertyMapper getMapper() {
			return mapper;
		}

		public ErrorBehavior getErrorBehavior() {
			return errorBehavior;
		}
	}
}
