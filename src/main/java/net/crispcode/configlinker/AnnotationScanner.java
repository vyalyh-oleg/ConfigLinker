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

import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.annotations.BoundProperty;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.enums.TrackPolicy;
import net.crispcode.configlinker.enums.Whitespaces;
import net.crispcode.configlinker.exceptions.AnnotationAnalyzeException;
import net.crispcode.configlinker.exceptions.PropertyMapException;
import net.crispcode.configlinker.mappers.IPropertyMapper;
import net.crispcode.configlinker.mappers.MapperFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


final class AnnotationScanner
{
	/**
	 * Property name pattern for '${var}' constructions.
	 */
	private static final Pattern variablePattern = Pattern.compile("\\$\\{\\w+\\}");
	/**
	 * Property name pattern for '@{param}' constructions.
	 */
	private static final Pattern dynamicMethodParameterPattern = Pattern.compile("@\\{\\w+\\}");
	
	private final FactorySettingsBuilder configBuilder;
	private final HashMap<Class<?>, ConfigDescription> configDescriptions;
	
	AnnotationScanner(FactorySettingsBuilder configBuilder)
	{
		this.configBuilder = configBuilder;
		this.configDescriptions = new HashMap<>();
	}
	
	HashMap<Class<?>, ConfigDescription> scan(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException
	{
		for (Class<?> configInterface : configInterfaces)
		{
			ConfigDescription configInterfaceDescription = validateConfigInterface(configInterface);
			
			HashMap<Method, ConfigDescription.PropertyDescription> boundPropertyMethods = new HashMap<>();
			Method[] methods = configInterface.getMethods();
			for (Method method : methods)
			{
				ConfigDescription.PropertyDescription propertyMethodDescription = validateConfigInterfaceMethod(method, configInterfaceDescription);
				if (propertyMethodDescription == null)
					continue;
				boundPropertyMethods.put(method, propertyMethodDescription);
			}
			
			if (boundPropertyMethods.isEmpty())
				throw new AnnotationAnalyzeException(
					"Interface '" + configInterface.getName() + "' doesn't contain any methods annotated with '@" + BoundProperty.class.getSimpleName() + "'.")
					.logAndReturn();
			
			configInterfaceDescription.setBoundPropertyMethods(boundPropertyMethods);
			configDescriptions.put(configInterface, configInterfaceDescription);
		}
		return configDescriptions;
	}
	
	private ConfigDescription validateConfigInterface(Class<?> configInterface) throws AnnotationAnalyzeException
	{
		BoundObject boundObjectAnnotation = configInterface.getDeclaredAnnotation(BoundObject.class);
		
		if (!configInterface.isInterface())
		{
			throw new AnnotationAnalyzeException("'" + configInterface.getName() + "' is not interface.").logAndReturn();
		}
		
		if (boundObjectAnnotation == null)
		{
			throw new AnnotationAnalyzeException("'" + configInterface.getName() + "' is not annotated with '@" + BoundObject.class.getSimpleName() + "'.")
				.logAndReturn();
		}
		
		
		// validate variables in @BoundObject#sourcePath
		String rawSourcePath = boundObjectAnnotation.sourcePath();
		String sourcePath;
		try
		{
			sourcePath = validateAndMakeVariableSubstitution(rawSourcePath);
		}
		catch (IllegalArgumentException e)
		{
			throw new AnnotationAnalyzeException("Syntax error in '@BoundObject.sourcePath()' value; interface '" + configInterface.getName() + "'.", e)
				.logAndReturn();
		}
		
		
		// validate variables in @BoundObject#defaultSourcePath
		String defaultSourcePath = boundObjectAnnotation.defaultSourcePath().isEmpty() ? null : boundObjectAnnotation.defaultSourcePath();
		
		
		// validate variables in @BoundObject#propertyNamePrefix
		String rawPropertyNamePrefix = boundObjectAnnotation.propertyNamePrefix();
		String propertyNamePrefix;
		if (rawPropertyNamePrefix.isEmpty())
			propertyNamePrefix = null;
		else
			try
			{
				propertyNamePrefix = validateAndMakeVariableSubstitution(rawPropertyNamePrefix);
			}
			catch (IllegalArgumentException e)
			{
				throw new AnnotationAnalyzeException(
					"Syntax error in '@BoundObject.propertyNamePrefix()' value; interface  '" + configInterface.getName() + "'.", e).logAndReturn();
			}
		
		
		// get SourceScheme
		SourceScheme sourceScheme = boundObjectAnnotation.sourceScheme();
		if (sourceScheme == SourceScheme.INHERIT)
			sourceScheme = configBuilder.getSourceScheme();
		
		
		// get and validate http headers
		Map<String, String> httpHeaders = null;
		String[] customHttpHeaders = boundObjectAnnotation.httpHeaders();
		boolean headersInAnnotation = !(customHttpHeaders.length == 1 && customHttpHeaders[0].isEmpty());
		
		if (headersInAnnotation && sourceScheme != SourceScheme.HTTP)
			throw new AnnotationAnalyzeException(
				"Setting custom http headers in '@BoundObject.httpHeaders()' is allowed only if 'BoundObject.SourceScheme == SourceScheme.HTTP'; interface  '"
					+ configInterface.getName() + "'.")
				.logAndReturn();
		
		if (sourceScheme == SourceScheme.HTTP)
		{
			httpHeaders = new HashMap<>();
			for (Map.Entry<String, String> headerEntry : this.configBuilder.getHttpHeaders().entrySet())
			{
				if (!headerEntry.getKey().trim().isEmpty())
				{
					String headerName = validateAndMakeVariableSubstitution(headerEntry.getKey());
					String headerValue = validateAndMakeVariableSubstitution(headerEntry.getValue());
					httpHeaders.put(headerName.trim().toLowerCase(), headerValue);
				}
				else
					throw new AnnotationAnalyzeException(
						"Syntax error in headers that was put into 'FactorySettingsBuilder', header '" + headerEntry.getKey() + ":" + headerEntry.getValue()
							+ "' has empty name; interface '" + configInterface.getName())
						.logAndReturn();
			}
		}
		
		if (headersInAnnotation)
		{
			String header;
			int colonIndex;
			for (String httpHeader : customHttpHeaders)
			{
				try
				{
					header = validateAndMakeVariableSubstitution(httpHeader);
					colonIndex = header.indexOf(':');
					if (colonIndex == -1 || header.substring(0, colonIndex).trim().isEmpty())
					{
						throw new AnnotationAnalyzeException(
							"Syntax error in '@BoundObject.httpHeaders()', header '" + httpHeader + "'; interface '" + configInterface.getName()
								+ "'; header name and value should be separated with colon ':' and the header name cannot be empty.")
							.logAndReturn();
					}
					httpHeaders.put(header.substring(0, colonIndex).trim().toLowerCase(), header.substring(colonIndex + 1).trim());
				}
				catch (IllegalArgumentException e)
				{
					throw new AnnotationAnalyzeException(
						"Syntax error in '@BoundObject.httpHeaders()': '" + httpHeader + "' value; interface '" + configInterface.getName() + "'.", e)
						.logAndReturn();
				}
			}
		}
		
		// get charset of raw configuration text
		String charsetName = boundObjectAnnotation.charsetName();
		Charset charset;
		if (charsetName.isEmpty())
			charset = configBuilder.getCharset();
		else
			try
			{
				charset = Charset.forName(charsetName);
			}
			catch (UnsupportedCharsetException e)
			{
				throw new AnnotationAnalyzeException("Charset with name '" + charsetName + "' not found.", e).logAndReturn();
			}
		
		
		// get TrackPolicy
		TrackPolicy trackPolicy = boundObjectAnnotation.trackingPolicy();
		if (trackPolicy == TrackPolicy.INHERIT)
			trackPolicy = configBuilder.getTrackPolicy();
		
		
		// get tracking interval
		int trackingInterval = boundObjectAnnotation.trackingInterval();
		if (sourceScheme != SourceScheme.HTTP || trackPolicy == TrackPolicy.DISABLE)
			trackingInterval = -1;
		else if (trackingInterval == 0)
			trackingInterval = configBuilder.getTrackingInterval();
		
		
		// get and check ConfigChangeListeners
		Class<? extends IConfigChangeListener> changeListener_class = boundObjectAnnotation.changeListener();
		IConfigChangeListener changeListener = null;
		if (changeListener_class != IConfigChangeListener.class)
		{
			try
			{
				Constructor<? extends IConfigChangeListener> constructor = changeListener_class.getDeclaredConstructor();
				constructor.setAccessible(true);
				changeListener = constructor.newInstance();
			}
			catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
			{
				throw new AnnotationAnalyzeException(
					"Cannot create '" + IConfigChangeListener.class.getSimpleName() + "' object; config interface '" + configInterface.getName() + "'.", e)
					.logAndReturn();
			}
		}
		if (changeListener != null && trackPolicy == TrackPolicy.DISABLE)
		{
			throw new AnnotationAnalyzeException(
				"You cannot use @BoundObject.changeListener() if TrackPolicy is 'DISABLE', config interface '" + configInterface.getName() + "'.")
				.logAndReturn();
		}
		
		
		// get ErrorBehaviour
		ErrorBehavior errorBehavior = boundObjectAnnotation.errorBehavior();
		if (errorBehavior == ErrorBehavior.INHERIT)
			errorBehavior = configBuilder.getErrorBehavior();
		
		if ((errorBehavior == ErrorBehavior.TRY_DEFAULTS_OR_NULL || errorBehavior == ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION) && defaultSourcePath == null)
			throw new AnnotationAnalyzeException(
				"You cannot use ErrorBehavior.TRY_DEFAULTS_OR_NULL or ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION without @BoundObject.defaultSourcePath(), config interface '" + configInterface
					.getName() + "'.")
				.logAndReturn();
		
		
		// get ignoreWhiteSpaces
		boolean ignoreWhiteSpaces = configBuilder.getWhitespaces() == Whitespaces.IGNORE;
		
		
		// create ConfigDescription
		ConfigDescription configDescription = new ConfigDescription(configInterface);
		configDescription.setSourcePath(sourcePath);
		configDescription.setDefaultSourcePath(defaultSourcePath);
		configDescription.setPropertyNamePrefix(propertyNamePrefix);
		configDescription.setSourceScheme(sourceScheme);
		configDescription.setHttpHeaders(httpHeaders);
		configDescription.setCharset(charset);
		configDescription.setIgnoreWhitespaces(ignoreWhiteSpaces);
		configDescription.setTrackPolicy(trackPolicy);
		configDescription.setTrackingInterval(trackingInterval);
		configDescription.setConfigChangeListener(changeListener);
		configDescription.setErrorBehavior(errorBehavior);
		
		return configDescription;
	}
	
	/**
	 * @param searchPattern Regexp pattern {@link AnnotationScanner#variablePattern} or {@link AnnotationScanner#dynamicMethodParameterPattern}
	 * @param template      Property name template with '${var}' or '@{param}' constructions.
	 * @return The unique set of variable names used in template. Empty array, if didn't find any variable.
	 */
	private LinkedHashSet<String> findVariables(Pattern searchPattern, String template)
	{
		Matcher matcher = searchPattern.matcher(template);
		LinkedHashSet<String> findElements = new LinkedHashSet<>();
		
		while (matcher.find())
		{
			findElements.add(matcher.group());
		}
		
		return findElements;
	}
	
	private String validateAndMakeVariableSubstitution(String parameterNamePattern) throws AnnotationAnalyzeException
	{
		LinkedHashSet<String> templateVars = findVariables(variablePattern, parameterNamePattern);
		for (String tVar : templateVars)
		{
			String varName = tVar.substring(2, tVar.length() - 1);
			String varValue = configBuilder.getParameters().get(varName);
			if (varValue == null)
			{
				throw new IllegalArgumentException("Not found value for variable '" + tVar + "'.");
			}
			parameterNamePattern = parameterNamePattern.replaceAll("\\$\\{" + varName + "\\}", varValue);
		}
		return parameterNamePattern;
	}
	
	/**
	 * @param parameterNameTemplate Property name template with '@{param}' constructions.
	 * @param method                Method from configuration interface.
	 * @return The unique set of dynamic variable names, used in method signature.
	 */
	private LinkedHashSet<String> validateDynamicVariables(String parameterNameTemplate, Method method) throws AnnotationAnalyzeException
	{
		LinkedHashSet<String> dynamicParameters = findVariables(dynamicMethodParameterPattern, parameterNameTemplate);
		Parameter[] methodParameters = method.getParameters();
		LinkedHashSet<String> parameterNames = new LinkedHashSet<>();
		
		for (Parameter mp : methodParameters)
		{
			Class<?> parameterType = mp.getType();
			if (!(parameterType == String.class || parameterType.isEnum()))
			{
				String fullMethodName = method.getDeclaringClass().getName() + "::" + method.getName();
				throw new AnnotationAnalyzeException(
					"Syntax error in '@BoundProperty.name()' value. Wrong signature in method '" + fullMethodName
						+ "': it should use only String or Enum types as arguments, but found '" + parameterType.getName() + "'.")
					.logAndReturn();
			}
			
			String parameterName = mp.getName();
			
			if (!dynamicParameters.contains("@{" + parameterName + "}"))
			{
				String fullMethodName = method.getDeclaringClass().getName() + "::" + method.getName();
				throw new AnnotationAnalyzeException(
					"Syntax error in '@BoundProperty.name()' value. Incompatible signature in method '" + fullMethodName
						+ "': parameter name '@{" + parameterName + "}' doesn't present in template '" + parameterNameTemplate + "'.")
					.logAndReturn();
			}
			
			parameterNames.add(parameterName);
		}
		
		for (String dParam : dynamicParameters)
		{
			String paramName = dParam.substring(2, dParam.length() - 1);
			
			if (!parameterNames.contains(paramName))
			{
				String fullMethodName = method.getDeclaringClass().getName() + "::" + method.getName();
				throw new AnnotationAnalyzeException(
					"Syntax error in '@BoundProperty.name()' value. Incompatible signature in method '" + fullMethodName + "': template variable '" + dParam
						+ "' doesn't present in method parameters.")
					.logAndReturn();
			}
		}
		
		return parameterNames;
	}
	
	private ConfigDescription.PropertyDescription validateConfigInterfaceMethod(Method propertyMethod, ConfigDescription configDescription) throws
		AnnotationAnalyzeException, PropertyMapException
	{
		BoundProperty boundPropertyAnnotation = propertyMethod.getDeclaredAnnotation(BoundProperty.class);
		
		if (boundPropertyAnnotation == null)
			return null;
		
		final String methodName = propertyMethod.getName();
		final String fullMethodName = propertyMethod.getDeclaringClass().getName() + "." + methodName;
		
		if (propertyMethod.isDefault())
			throw new AnnotationAnalyzeException("Method '" + fullMethodName + "', annotated with @BoundProperty, can not be default.").logAndReturn();
		
		if (Modifier.isStatic(propertyMethod.getModifiers()))
			throw new AnnotationAnalyzeException("Method '" + fullMethodName + "', annotated with @BoundProperty, can not be static.").logAndReturn();
		
		String propertyNameTemplate;
		try
		{
			propertyNameTemplate = validateAndMakeVariableSubstitution(boundPropertyAnnotation.name());
		}
		catch (IllegalArgumentException e)
		{
			throw new AnnotationAnalyzeException("Syntax error in '@BoundProperty.name()' value, method '" + fullMethodName + "'.", e).logAndReturn();
		}
		if (configDescription.getPropertyNamePrefix() != null && propertyNameTemplate.startsWith("."))
			propertyNameTemplate = configDescription.getPropertyNamePrefix() + propertyNameTemplate;
		
		LinkedHashSet<String> methodParameterNames = validateDynamicVariables(propertyNameTemplate, propertyMethod);
		String[] propertyDynamicVariableNames = methodParameterNames.isEmpty() ? null : methodParameterNames.toArray(new String[methodParameterNames.size()]);
		
		ErrorBehavior errorBehavior = boundPropertyAnnotation.errorBehavior();
		if (errorBehavior == ErrorBehavior.INHERIT)
			errorBehavior = configDescription.getErrorBehavior();
		
		boolean ignoreWhiteSpaces = configDescription.isIgnoreWhitespaces();
		Whitespaces whitespaces = boundPropertyAnnotation.whitespaces();
		if (whitespaces != Whitespaces.INHERIT)
			ignoreWhiteSpaces = whitespaces == Whitespaces.IGNORE;
		
		IPropertyMapper propertyMapper = MapperFactory.create(boundPropertyAnnotation, propertyMethod, ignoreWhiteSpaces);
		
		ConfigDescription.PropertyDescription propertyMethodDescription = configDescription.new PropertyDescription(propertyNameTemplate,
			propertyDynamicVariableNames, propertyMapper, errorBehavior);
		
		return propertyMethodDescription;
	}
}
