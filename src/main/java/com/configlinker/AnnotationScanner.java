package com.configlinker;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.AnnotationAnalyzeException;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.mappers.MapperFactory;
import com.configlinker.mappers.PropertyMapper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


final class AnnotationScanner {
	/**
	 * Property name pattern for '${var}' constructions.
	 */
	private static final Pattern variablePattern = Pattern.compile("\\$\\{\\w+\\}");
	/**
	 * Property name pattern for '@{param}' constructions.
	 */
	private static final Pattern dynamicVariablePattern = Pattern.compile("@\\{\\w+\\}");

	private final ConfigSetBuilder configBuilder;
	private final HashMap<Class<?>, ConfigDescription> configDescriptions;

	AnnotationScanner(ConfigSetBuilder configBuilder) {
		this.configBuilder = configBuilder;
		this.configDescriptions = new HashMap<>();
	}

	HashMap<Class<?>, ConfigDescription> scan(Set<Class<?>> configInterfaces) throws AnnotationAnalyzeException, PropertyMapException {
		for (Class<?> configInterface : configInterfaces) {
			ConfigDescription configInterfaceDescription = validateConfigInterface(configInterface);

			HashMap<Method, ConfigDescription.PropertyDescription> boundPropertyMethods = new HashMap<>();
			Method[] methods = configInterface.getMethods();
			for (Method method : methods) {
				ConfigDescription.PropertyDescription propertyMethodDescription = validateConfigInterfaceMethod(method, configInterfaceDescription);
				if (propertyMethodDescription == null)
					continue;
				boundPropertyMethods.put(method, propertyMethodDescription);
			}

			if (boundPropertyMethods.isEmpty()) {
				// TODO: log
				throw new AnnotationAnalyzeException("Interface '" + configInterface.getName() + "' doesn't contain any methods annotated with '@" + BoundProperty.class.getSimpleName() + "'.");
			}

			configInterfaceDescription.setBoundPropertyMethods(boundPropertyMethods);
			configDescriptions.put(configInterface, configInterfaceDescription);
		}
		return configDescriptions;
	}

	private ConfigDescription validateConfigInterface(Class<?> configInterface) throws AnnotationAnalyzeException {
		BoundObject boundObjectAnnotation = configInterface.getDeclaredAnnotation(BoundObject.class);

		if (!configInterface.isInterface()) {
			// TODO: make log record
			throw new AnnotationAnalyzeException("'" + configInterface.getName() + "' is not interface.");
		}

		if (boundObjectAnnotation == null) {
			// TODO: make log record
			throw new AnnotationAnalyzeException("'" + configInterface.getName() + "' is not annotated with '@" + BoundObject.class.getSimpleName() + "'.");
		}


		// validate variables in @BoundObject#sourcePath
		String rawSourcePath = boundObjectAnnotation.sourcePath();
		String sourcePath;
		try {
			sourcePath = validateAndMakeVariableSubstitution(rawSourcePath);
		} catch (AnnotationAnalyzeException e) {
			// TODO:log
			throw new AnnotationAnalyzeException("Syntax error in '@BoundObject.sourcePath()' value; interface  '" + configInterface.getName() + "'.", e);
		}


		// validate variables in @BoundObject#propertyNamePrefix
		String rawPropertyNamePrefix = boundObjectAnnotation.propertyNamePrefix();
		String propertyNamePrefix;
		try {
			propertyNamePrefix = validateAndMakeVariableSubstitution(rawPropertyNamePrefix);
		} catch (AnnotationAnalyzeException e) {
			// TODO:log
			throw new AnnotationAnalyzeException("Syntax error in '@BoundObject.propertyNamePrefix()' value; interface  '" + configInterface.getName() + "'.", e);
		}


		// get SourceScheme
		BoundObject.SourceScheme sourceScheme = boundObjectAnnotation.sourceScheme();
		if (sourceScheme == BoundObject.SourceScheme.INHERIT)
			sourceScheme = configBuilder.getSourceScheme();


		// get and validate http headers
		String[] customHttpHeaders = boundObjectAnnotation.httpHeaders();
		if (!(customHttpHeaders.length == 1 && customHttpHeaders[0].isEmpty()) && sourceScheme != BoundObject.SourceScheme.HTTP)
			throw new AnnotationAnalyzeException("Setting custom http headers in '@BoundObject.httpHeaders()' is allowed only if 'BoundObject.SourceScheme == SourceScheme.HTTP'; interface  '" + configInterface.getName() + "'.");

		Map<String, String> httpHeaders = null;
		if (sourceScheme == BoundObject.SourceScheme.HTTP) {
			httpHeaders = new HashMap<>();
			httpHeaders.putAll(this.configBuilder.getHttpHeaders());
			String header;
			int colonIndex;
			for (int i = 0; i < customHttpHeaders.length; i++)
				try {
					header = validateAndMakeVariableSubstitution(customHttpHeaders[i]);
					colonIndex = header.indexOf(':');
					if (colonIndex == -1) {
						throw new AnnotationAnalyzeException("Syntax error in '@BoundObject.httpHeaders()': '" + customHttpHeaders[i] + "' value; interface '" + configInterface.getName() + "': header name and value should be separated with colon ':'");
					}
					httpHeaders.put(header.substring(0, colonIndex).trim(),
									header.substring(colonIndex + 1, header.length()).trim());
				} catch (AnnotationAnalyzeException e) {
					// TODO:log
					throw new AnnotationAnalyzeException("Syntax error in '@BoundObject.httpHeaders()': '" + customHttpHeaders[i] + "' value; interface '" + configInterface.getName() + "'.", e);
				}
		}


		// get charset of raw configuration text
		String charsetName = boundObjectAnnotation.charsetName();
		Charset charset;
		if (charsetName.isEmpty())
			charset = configBuilder.getCharset();
		else
			try {
				charset = Charset.forName(charsetName);
			} catch (UnsupportedCharsetException e) {
				// TODO: log
				throw new AnnotationAnalyzeException("Charset with name '" + charsetName + "' not found.", e);
			}


		// get TrackPolicy
		BoundObject.TrackPolicy trackPolicy = boundObjectAnnotation.trackPolicy();
		if (trackPolicy == BoundObject.TrackPolicy.INHERIT)
			trackPolicy = configBuilder.getTrackPolicy();

		if (trackPolicy == BoundObject.TrackPolicy.ENABLE && sourceScheme == BoundObject.SourceScheme.CLASSPATH) {
			String msg = "You can not track changes for " + BoundObject.SourceScheme.class.getSimpleName() + "=='" + BoundObject.SourceScheme.CLASSPATH.name() + "', config interface '" + configInterface.getName() + "'; please, use for such purposes '" + BoundObject.SourceScheme.FILE.name() + "'.";
			// TODO: log
			throw new AnnotationAnalyzeException(msg);
		}


		// get tracking interval
		int trackingInterval = boundObjectAnnotation.trackingInterval();
		if (sourceScheme != BoundObject.SourceScheme.HTTP || trackPolicy == BoundObject.TrackPolicy.DISABLE)
			trackingInterval = -1;
		else if (trackingInterval == 0)
			trackingInterval = configBuilder.getTrackingInterval();


		// get and check ConfigChangeListener
		Class<? extends ConfigChangeListener> changeListener_class = boundObjectAnnotation.changeListener();
		ConfigChangeListener changeListener = null;
		if (changeListener_class != ConfigChangeListener.class)
			try {
				changeListener_class.getDeclaredConstructor().setAccessible(true);
				changeListener = changeListener_class.newInstance();
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				// TODO: log
				throw new AnnotationAnalyzeException("Cannot create '" + ConfigChangeListener.class.getSimpleName() + "' object; config interface '" + configInterface.getName() + "'.", e);
			}
		if (changeListener != null && trackPolicy == BoundObject.TrackPolicy.DISABLE) {
			// TODO: log
			throw new AnnotationAnalyzeException("You cannot use @BoundObject.changeListener() if TrackPolicy is 'DISABLE', config interface '" + configInterface.getName() + "'.");
		}


		// get ErrorBehaviour
		ErrorBehavior errorBehavior = boundObjectAnnotation.errorBehavior();
		if (errorBehavior == ErrorBehavior.INHERITED)
			errorBehavior = configBuilder.getErrorBehavior();


		// create ConfigDescription
		ConfigDescription configDescription = new ConfigDescription(configInterface);
		configDescription.setSourcePath(sourcePath);
		configDescription.setPropertyNamePrefix(propertyNamePrefix);
		configDescription.setSourceScheme(sourceScheme);
		configDescription.setHttpHeaders(httpHeaders);
		configDescription.setCharset(charset);
		configDescription.setTrackPolicy(trackPolicy);
		configDescription.setTrackingInterval(trackingInterval);
		configDescription.setConfigChangeListener(changeListener);
		configDescription.setErrorBehavior(errorBehavior);

		return configDescription;
	}

	/**
	 * @param searchPattern Regexp pattern {@link AnnotationScanner#variablePattern} or {@link AnnotationScanner#dynamicVariablePattern}
	 * @param template      Property name template with '${var}' or '@{param}' constructions.
	 * @return The unique set of variable names used in template. Empty array, if didn't find any variable.
	 */
	private String[] findVariables(Pattern searchPattern, String template) {
		Matcher matcher = searchPattern.matcher(template);
		HashSet<String> findElements = new HashSet<>();
		while (matcher.find())
			findElements.add(matcher.group());
		return findElements.toArray(new String[findElements.size()]);
	}

	private String validateAndMakeVariableSubstitution(String parameterNamePattern) throws AnnotationAnalyzeException {
		String[] templateVars = findVariables(variablePattern, parameterNamePattern);
		for (String tVar : templateVars) {
			String varValue = configBuilder.getParameters().get(tVar);
			if (varValue == null) {
				// TODO: log
				throw new AnnotationAnalyzeException("Not found value for variable '" + tVar + "'.");
			}
			parameterNamePattern = parameterNamePattern.replaceAll("\\$\\{" + tVar + "\\}", varValue);
		}
		return parameterNamePattern;
	}

	/**
	 * @param parameterNameTemplate Property name template with '@{param}' constructions.
	 * @param method Method from configuration interface.
	 * @return The unique set of dynamic variable names, used in method signature.
	 */
	private String[] validateDynamicVariables(String parameterNameTemplate, Method method) throws AnnotationAnalyzeException {
		String[] dynamicVars = findVariables(dynamicVariablePattern, parameterNameTemplate);
		Parameter[] methodParameters = method.getParameters();
		String[] parameterNames = new String[methodParameters.length];

		for (int i = 0; i < methodParameters.length; i++) {
			Parameter mp = methodParameters[i];
			Class<?> parameterType = mp.getType();
			if (!(parameterType == String.class || parameterType.isEnum())) {
				// TODO: log
				throw new AnnotationAnalyzeException("Wrong signature in method '" + method.getName() + "': it should use only String or Enum types as arguments, but found '" + parameterType.getName() + "'.");
			}

			String parameterName = mp.getName();
			parameterNames[i] = parameterName;
			boolean isPresentInVars = Stream.of(dynamicVars).anyMatch(parameterName::equals);
			if (!isPresentInVars) {
				// TODO: log
				throw new AnnotationAnalyzeException("Incompatible signature in method '" + method.getName() + "': parameter name '" + parameterName + "' doesn't present in template '" + parameterNameTemplate + "'.");
			}
		}

		for (String varName : dynamicVars) {
			boolean isPresentInParameters = Stream.of(parameterNames).anyMatch(varName::equals);
			if (!isPresentInParameters) {
				// TODO: log
				throw new AnnotationAnalyzeException("Incompatible signature in method '" + method.getName() + "': template variable '@{" + varName + "}' doesn't present in method parameters.");
			}
		}

		return dynamicVars;
	}

	private ConfigDescription.PropertyDescription validateConfigInterfaceMethod(Method propertyMethod, ConfigDescription configDescription) throws AnnotationAnalyzeException, PropertyMapException {
		BoundProperty boundPropertyAnnotation = propertyMethod.getDeclaredAnnotation(BoundProperty.class);
		if (boundPropertyAnnotation == null)
			return null;

		final String fullMethodName = propertyMethod.getDeclaringClass().getName() + "." + propertyMethod.getName();

		if (Modifier.isStatic(propertyMethod.getModifiers())) {
			// TODO: log
			throw new AnnotationAnalyzeException("Method '" + fullMethodName + "', annotated with @BoundProperty, can not be static.");
		}

		String propertyNameTemplate;
		try {
			propertyNameTemplate = validateAndMakeVariableSubstitution(boundPropertyAnnotation.name());
		} catch (AnnotationAnalyzeException e) {
			// TODO:log
			throw new AnnotationAnalyzeException("Syntax error in '@BoundProperty.name()' value, method '" + fullMethodName + "'.", e);
		}

		String[] propertyDynamicVariableNames;
		try {
			propertyDynamicVariableNames = validateDynamicVariables(propertyNameTemplate, propertyMethod);
			if (propertyDynamicVariableNames.length == 0)
				propertyDynamicVariableNames = null;
		} catch (AnnotationAnalyzeException e) {
			// TODO:log
			throw new AnnotationAnalyzeException("Syntax error in '@BoundProperty.name()' value, method '" + fullMethodName + "'.", e);
		}

		Class<?> returnType = propertyMethod.getReturnType();
		PropertyMapper propertyMapper = MapperFactory.create(returnType, boundPropertyAnnotation, propertyMethod);

		ErrorBehavior errorBehavior = boundPropertyAnnotation.errorBehavior();
		if (errorBehavior == ErrorBehavior.INHERITED)
			errorBehavior = configDescription.getErrorBehavior();


		ConfigDescription.PropertyDescription propertyMethodDescription = new ConfigDescription.PropertyDescription();
		propertyMethodDescription.setName(propertyNameTemplate);
		propertyMethodDescription.setDynamicVariableNames(propertyDynamicVariableNames);
		propertyMethodDescription.setMapper(propertyMapper);
		propertyMethodDescription.setErrorBehavior(errorBehavior);

		return propertyMethodDescription;
	}

	private void validateNestedBoundProperty() {
		// TODO: implement functionality of parsing nested @BoundProperty classes or remove this method.
	}

}
