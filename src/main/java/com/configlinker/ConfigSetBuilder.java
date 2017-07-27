package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.exceptions.ConfigSetBuilderClosedException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class ConfigSetBuilder {
	private Map<String, String> parameters = new HashMap<>();
	private BoundObject.SourceScheme sourceScheme = BoundObject.SourceScheme.FILE;
	private BoundObject.TrackPolicy trackPolicy = BoundObject.TrackPolicy.DISABLE;
	private int trackingInterval = 60;
	private Charset charset = StandardCharsets.UTF_8;
	private ErrorBehavior errorBehavior = ErrorBehavior.THROW_EXCEPTION;
	private boolean closed = false;

	/**
	 * String parameters, used for substitution in {@link BoundObject#sourcePath}, {@link BoundObject#propertyNamePrefix} and {@link com.configlinker.annotations.BoundProperty#name}.
	 */
	public ConfigSetBuilder addParameter(String key, String value) throws ConfigSetBuilderClosedException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		parameters.put(key, value);
		return this;
	}

	/**
	 * See {@link BoundObject.SourceScheme} and {@link BoundObject#sourceScheme()}
	 */
	public ConfigSetBuilder setSourceScheme(BoundObject.SourceScheme sourceScheme) throws ConfigSetBuilderClosedException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		this.sourceScheme = sourceScheme;
		return this;
	}

	/**
	 * See {@link BoundObject.TrackPolicy} and {@link BoundObject#trackPolicy()}
	 */
	public ConfigSetBuilder setTrackPolicy(BoundObject.TrackPolicy trackPolicy) throws ConfigSetBuilderClosedException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		this.trackPolicy = trackPolicy;
		return this;
	}

	/**
	 * This property has sense only if you use {@link BoundObject.SourceScheme#HTTP} here (in builder) or in {@link BoundObject#sourceScheme()} on one of configuration interfaces.
	 *
	 * @param trackingInterval Parameter reread interval in seconds. MIN value = 15 seconds, MAX value = 24 hours (24*3600 seconds).
	 */
	public ConfigSetBuilder setTrackingInterval(int trackingInterval) throws ConfigSetBuilderClosedException, IllegalArgumentException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		if (trackingInterval < 15 || trackingInterval > 24 * 3600)
			throw new IllegalArgumentException("Tracking interval can not be less than 15 seconds and greater than 24 hours. You set '" + trackingInterval + "'.");
		this.trackingInterval = trackingInterval;
		return this;
	}

	/**
	 * See {@link BoundObject#charsetName()}
	 */
	public ConfigSetBuilder setCharset(Charset charset) throws ConfigSetBuilderClosedException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		this.charset = charset;
		return this;
	}

	/**
	 * See {@link ErrorBehavior} and {@link BoundObject#errorBehavior()}
	 */
	public void setErrorBehavior(ErrorBehavior errorBehavior) throws ConfigSetBuilderClosedException {
		if (closed)
			throw ConfigSetBuilderClosedException.getInstance();
		this.errorBehavior = errorBehavior;
	}


	Map<String, String> getParameters() {
		return parameters;
	}

	BoundObject.SourceScheme getSourceScheme() {
		return sourceScheme;
	}

	BoundObject.TrackPolicy getTrackPolicy() {
		return trackPolicy;
	}

	int getTrackingInterval() {
		return trackingInterval;
	}

	Charset getCharset() {
		return charset;
	}

	ErrorBehavior getErrorBehavior() {
		return errorBehavior;
	}


	void close() {
		this.closed = true;
		this.parameters = Collections.unmodifiableMap(this.parameters);
	}

	boolean isClosed() {
		return closed;
	}
}
