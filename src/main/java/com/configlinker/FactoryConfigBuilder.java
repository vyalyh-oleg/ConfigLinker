package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.exceptions.FactoryConfigBuilderClosedException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Default configuration factory values:
 * <ul>
 * <li>sourceScheme = {@link BoundObject.SourceScheme#FILE}</li>
 * <li>trackPolicy = {@link BoundObject.TrackPolicy#DISABLE}</li>
 * <li>trackingInterval = 60</li>
 * <li>charset = {@link StandardCharsets#UTF_8}</li>
 * <li>errorBehavior = {@link ErrorBehavior#THROW_EXCEPTION}</li>
 * </ul>
 */
public final class FactoryConfigBuilder {
	private Map<String, String> parameters = new HashMap<>();
	private Map<String, String> httpHeaders = new HashMap<>();
	private BoundObject.SourceScheme sourceScheme = BoundObject.SourceScheme.FILE;
	private BoundObject.TrackPolicy trackPolicy = BoundObject.TrackPolicy.DISABLE;
	private int trackingInterval = 60;
	private Charset charset = StandardCharsets.UTF_8;
	private ErrorBehavior errorBehavior = ErrorBehavior.THROW_EXCEPTION;
	private boolean closed = false;

	/**
	 * <p>String parameters, used for substitution in {@link BoundObject#sourcePath}, {@link BoundObject#propertyNamePrefix}, {@link com.configlinker.annotations.BoundProperty#name} and {@link BoundObject#httpHeaders}.
	 * @param key Key
	 * @param value Value
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactoryConfigBuilder addParameter(String key, String value) throws FactoryConfigBuilderClosedException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		parameters.put(key, value);
		return this;
	}

	/**
	 * @param sourceScheme See {@link BoundObject.SourceScheme} and {@link BoundObject#sourceScheme()}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactoryConfigBuilder setSourceScheme(BoundObject.SourceScheme sourceScheme) throws FactoryConfigBuilderClosedException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.sourceScheme = sourceScheme;
		return this;
	}

	/**
	 * <p>Use this method to add necessary headers to every request that will be made to receive configuration. This headers are used only if the {@link BoundObject#sourceScheme} is {@link BoundObject.SourceScheme#HTTP}.
	 * This method do not merge values for the same header names. In duplicate case header will be simply replaced.
	 * @param name Header name
	 * @param value Header value
	 * @return this
	 */
	public FactoryConfigBuilder setHttpHeader(String name, String value){
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.httpHeaders.put(name, value);
		return this;
	}

	/**
	 * @param trackPolicy See {@link BoundObject.TrackPolicy} and {@link BoundObject#trackPolicy()}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactoryConfigBuilder setTrackPolicy(BoundObject.TrackPolicy trackPolicy) throws FactoryConfigBuilderClosedException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.trackPolicy = trackPolicy;
		return this;
	}

	/**
	 * <p>This property has sense only if you use {@link BoundObject.SourceScheme#HTTP} here (in builder) or in {@link BoundObject#sourceScheme()} on one of configuration interfaces.
	 *
	 * @param trackingInterval Parameter reread interval in seconds. MIN value = 15 seconds, MAX value = 24 hours (24*3600 seconds).
	 * @return -
	 */
	public FactoryConfigBuilder setTrackingInterval(int trackingInterval) throws FactoryConfigBuilderClosedException, IllegalArgumentException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		if (trackingInterval < 15 || trackingInterval > 24 * 3600)
			throw new IllegalArgumentException("Tracking interval can not be less than 15 seconds and greater than 24 hours. You set '" + trackingInterval + "'.");
		this.trackingInterval = trackingInterval;
		return this;
	}

	/**
	 * <p>See {@link BoundObject#charsetName()}
	 * @param charset The charset of the loaded property file.
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactoryConfigBuilder setCharset(Charset charset) throws FactoryConfigBuilderClosedException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.charset = charset;
		return this;
	}

	/**
	 * @param errorBehavior See {@link ErrorBehavior} and {@link BoundObject#errorBehavior()}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactoryConfigBuilder setErrorBehavior(ErrorBehavior errorBehavior) throws FactoryConfigBuilderClosedException {
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.errorBehavior = errorBehavior;
		return this;
	}


	Map<String, String> getParameters() {
		return parameters;
	}

	BoundObject.SourceScheme getSourceScheme() {
		return sourceScheme;
	}

	Map<String, String> getHttpHeaders() {
		return httpHeaders;
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
}
