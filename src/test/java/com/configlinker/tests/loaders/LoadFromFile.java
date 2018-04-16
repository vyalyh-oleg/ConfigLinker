package com.configlinker.tests.loaders;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject( sourcePath = "./configs/workdir_config.properties")
interface LoadFromFile
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
