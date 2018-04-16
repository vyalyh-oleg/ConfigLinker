package com.configlinker.tests.loaders;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "classpath_config.properties", sourceScheme = BoundObject.SourceScheme.CLASSPATH)
interface LoadFromClasspath
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
