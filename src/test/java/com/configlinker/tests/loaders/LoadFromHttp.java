package com.configlinker.tests.loaders;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "http://localhost:5050/configs/http_config.properties", sourceScheme = BoundObject.SourceScheme.HTTP)
interface LoadFromHttp
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
