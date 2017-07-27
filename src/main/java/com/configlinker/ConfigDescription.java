package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.mappers.PropertyMapper;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;


public final class ConfigDescription {
	private final Class<?> confInterface;
	private BoundObject.SourceScheme sourceScheme;
	private String sourcePath;
	private Charset charset;
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

	public String getSourcePath() {
		return sourcePath;
	}

	public Charset getCharset() {
		return charset;
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

	void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	void setCharset(Charset charset) {
		this.charset = charset;
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


	public static final class PropertyDescription {
		private String name;
		private String[] dynamicVariableNames; // can be null
		private PropertyMapper mapper;
		private ErrorBehavior errorBehavior;

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

		void setName(String name) {
			this.name = name;
		}

		/**
		 *
		 * @param dynamicVariableNames Can be null.
		 */
		void setDynamicVariableNames(String[] dynamicVariableNames) {
			this.dynamicVariableNames = dynamicVariableNames;
		}

		void setMapper(PropertyMapper mapper) {
			this.mapper = mapper;
		}

		void setErrorBehavior(ErrorBehavior errorBehavior) {
			this.errorBehavior = errorBehavior;
		}
	}
}
