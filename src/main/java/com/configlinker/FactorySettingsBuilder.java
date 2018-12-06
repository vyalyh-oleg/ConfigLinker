package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.enums.SourceScheme;
import com.configlinker.enums.TrackPolicy;
import com.configlinker.enums.Whitespaces;
import com.configlinker.exceptions.FactoryConfigBuilderClosedException;
import com.configlinker.exceptions.FactoryConfigBuilderException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Default configuration factory values:
 * <ul>
 * <li>sourceScheme = {@link SourceScheme#FILE}</li>
 * <li>trackingPolicy = {@link TrackPolicy#DISABLE}</li>
 * <li>trackingInterval = 60</li>
 * <li>charset = {@link StandardCharsets#UTF_8}</li>
 * <li>whitespaces = true</li>
 * <li>errorBehavior = {@link ErrorBehavior#THROW_EXCEPTION}</li>
 * </ul>
 */
public final class FactorySettingsBuilder
{
	private Map<String, String> parameters = new HashMap<>();
	private Map<String, String> httpHeaders = new HashMap<>();
	private SourceScheme sourceScheme = SourceScheme.FILE;
	private TrackPolicy trackPolicy = TrackPolicy.DISABLE;
	private int trackingInterval = 60;
	private Charset charset = StandardCharsets.UTF_8;
	private Whitespaces whitespaces = Whitespaces.IGNORE;
	private ErrorBehavior errorBehavior = ErrorBehavior.THROW_EXCEPTION;
	private boolean closed = false;
	
	public static FactorySettingsBuilder create()
	{
		return new FactorySettingsBuilder();
	}
	
	private FactorySettingsBuilder()
	{
	}
	
	/**
	 * <p>String parameters, used for substitution in {@link BoundObject#sourcePath}, {@link BoundObject#propertyNamePrefix}, {@link com.configlinker.annotations.BoundProperty#name} and {@link BoundObject#httpHeaders}.
	 *
	 * @param key   Key
	 * @param value Value
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactorySettingsBuilder addParameter(String key, String value) throws FactoryConfigBuilderClosedException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		
		if (key == null || key.isEmpty() || value == null || value.isEmpty())
			throw new FactoryConfigBuilderException("'key' or 'value' null or empty.");
		
		parameters.put(key, value);
		return this;
	}
	
	/**
	 * @param sourceScheme See {@link SourceScheme} and {@link BoundObject#sourceScheme()}. Default value: {@link SourceScheme#FILE}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactorySettingsBuilder setSourceScheme(SourceScheme sourceScheme) throws FactoryConfigBuilderClosedException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		
		if (sourceScheme == SourceScheme.INHERIT)
			this.sourceScheme = SourceScheme.FILE;
		else
			this.sourceScheme = sourceScheme;
		
		return this;
	}
	
	/**
	 * <p>Use this method to add necessary headers to every request that will be made to receive configuration. This headers are used only if the {@link BoundObject#sourceScheme} is {@link SourceScheme#HTTP}.
	 * This method do not merge values for the same header names. In duplicate case header will be simply replaced.
	 *
	 * @param name  Header name
	 * @param value Header value
	 * @return this
	 */
	public FactorySettingsBuilder setHttpHeader(String name, String value)
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		
		if (name != null && !name.isEmpty() && value != null)
			this.httpHeaders.put(name, value);
		
		return this;
	}
	
	/**
	 * @param trackPolicy See {@link TrackPolicy} and {@link BoundObject#trackingPolicy()}. Default value: {@link TrackPolicy#DISABLE}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactorySettingsBuilder setTrackPolicy(TrackPolicy trackPolicy) throws FactoryConfigBuilderClosedException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		
		if (trackPolicy == TrackPolicy.INHERIT)
			this.trackPolicy = TrackPolicy.DISABLE;
		else
			this.trackPolicy = trackPolicy;
		
		return this;
	}
	
	/**
	 * <p>This property has sense only if you use {@link SourceScheme#HTTP} here (in builder) or in {@link BoundObject#sourceScheme()} on one of the configuration interfaces.
	 * <p>Default: {@code 60} seconds
	 *
	 * @param trackingInterval reread interval in seconds. MIN value = 15 seconds, MAX value = 24 hours (24*3600 seconds).
	 * @return this
	 */
	public FactorySettingsBuilder setTrackingInterval(int trackingInterval) throws FactoryConfigBuilderClosedException, IllegalArgumentException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		if (trackingInterval < 15 || trackingInterval > 24 * 3600)
			throw new IllegalArgumentException(
			  "Tracking interval can not be less than 15 seconds and greater than 24 hours. You set '" + trackingInterval + "'.");
		this.trackingInterval = trackingInterval;
		return this;
	}
	
	/**
	 * <p>See {@link BoundObject#charsetName()}
	 *
	 * @param charset The charset of the loaded property file. Default value {@link StandardCharsets#UTF_8}
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactorySettingsBuilder setCharset(Charset charset) throws FactoryConfigBuilderClosedException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.charset = charset;
		return this;
	}
	
	/**
	 * @param whitespaces See {@link Whitespaces}. Default value {@link Whitespaces#IGNORE}
	 * @return this
	 */
	public FactorySettingsBuilder setWhitespaces(Whitespaces whitespaces)
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		
		if (whitespaces == Whitespaces.INHERIT)
			this.whitespaces = Whitespaces.IGNORE;
		else
			this.whitespaces = whitespaces;
		
		return this;
	}
	
	/**
	 * @param errorBehavior See {@link BoundObject#errorBehavior()}. Default value {@link ErrorBehavior#THROW_EXCEPTION}.
	 * @return this
	 * @throws FactoryConfigBuilderClosedException -
	 */
	public FactorySettingsBuilder setErrorBehavior(ErrorBehavior errorBehavior) throws FactoryConfigBuilderClosedException
	{
		if (closed)
			throw FactoryConfigBuilderClosedException.getInstance();
		this.errorBehavior = errorBehavior;
		return this;
	}
	
	
	public Map<String, String> getParameters()
	{
		return parameters;
	}
	
	public SourceScheme getSourceScheme()
	{
		return sourceScheme;
	}
	
	public Map<String, String> getHttpHeaders()
	{
		return httpHeaders;
	}
	
	public TrackPolicy getTrackPolicy()
	{
		return trackPolicy;
	}
	
	public int getTrackingInterval()
	{
		return trackingInterval;
	}
	
	public Charset getCharset()
	{
		return charset;
	}
	
	public Whitespaces whitespaces()
	{
		return whitespaces;
	}
	
	public ErrorBehavior getErrorBehavior()
	{
		return errorBehavior;
	}
	
	
	void close()
	{
		this.closed = true;
		this.parameters = Collections.unmodifiableMap(this.parameters);
		this.httpHeaders = Collections.unmodifiableMap(this.httpHeaders);
	}
}
