package com.configlinker.tests.loaders;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "<ConfigLinker.server>", sourceScheme = BoundObject.SourceScheme.CONFIG_LINKER_SERVER)
interface LoadFromConfigLinkerServer
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
