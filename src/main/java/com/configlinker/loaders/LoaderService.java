package com.configlinker.loaders;

import com.configlinker.ConfigDescription;
import com.configlinker.annotations.BoundObject;
import com.configlinker.exceptions.PropertyLoadException;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


final public class LoaderService {
	private final Map<BoundObject.SourceScheme, ALoader> loaders;

	public LoaderService(EnumMap<BoundObject.SourceScheme, ALoader> loaders) {
		this.loaders = loaders;
	}

	public static LoaderService create(HashMap<Class<?>, ConfigDescription> configDescriptions) {
		EnumMap<BoundObject.SourceScheme, HashMap<Class<?>, ConfigDescription>> schemeDescriptions = configDescriptions.values().stream()
		.collect(
				() -> new EnumMap<BoundObject.SourceScheme, HashMap<Class<?>, ConfigDescription>>(BoundObject.SourceScheme.class),
				(map, confDescr) -> map.computeIfAbsent(confDescr.getSourceScheme(), srcScheme -> new HashMap<>()).put(confDescr.getConfInterface(), confDescr),
			EnumMap::putAll);

		EnumMap<BoundObject.SourceScheme, ALoader> loaders = new EnumMap<>(BoundObject.SourceScheme.class);

		HashMap<Class<?>, ConfigDescription> descriptions = schemeDescriptions.get(BoundObject.SourceScheme.CLASSPATH);
		if (descriptions != null)
			loaders.put(BoundObject.SourceScheme.CLASSPATH, new ClasspathLoader(descriptions));

		descriptions = schemeDescriptions.get(BoundObject.SourceScheme.FILE);
		if (descriptions != null)
			loaders.put(BoundObject.SourceScheme.FILE, new PropertyFileLoader(descriptions));

		descriptions = schemeDescriptions.get(BoundObject.SourceScheme.HTTP);
		if (descriptions != null)
			loaders.put(BoundObject.SourceScheme.HTTP, new HttpLoader(descriptions));

		descriptions = schemeDescriptions.get(BoundObject.SourceScheme.CONFIG_LINKER_SERVER);
		if (descriptions != null)
			loaders.put(BoundObject.SourceScheme.CONFIG_LINKER_SERVER, new ConfigLinkerLoader(descriptions));

		return new LoaderService(loaders);
	}

	public Object getProperty(ConfigDescription configDescription, ConfigDescription.PropertyDescription propertyDescription, Method method, Object[] proxyArgs) {
		HashMap<String, String> methodArguments = null;
		if (propertyDescription.getDynamicVariableNames() != null)
			if (propertyDescription.getDynamicVariableNames().length != proxyArgs.length)
				throw new PropertyLoadException("No obligatory arguments for method '" + configDescription.getConfInterface().getName() + "#" + method.getName() + "'.");
			else {
				methodArguments = new HashMap<>();
				for (int i = 0; i < propertyDescription.getDynamicVariableNames().length; i++)
					methodArguments.put(propertyDescription.getDynamicVariableNames()[i], proxyArgs[i].toString());
			}

		return loaders.get(configDescription.getSourceScheme()).getProperty(configDescription, propertyDescription, method, methodArguments);
	}
}