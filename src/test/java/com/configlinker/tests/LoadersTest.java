package com.configlinker.tests;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


class LoadersTest extends AbstractBaseTest
{
	@Test
	void test_ClasspathLoader()
	{
		LoadFromClasspath loadFromClasspath = getSingleConfigInstance(LoadFromClasspath.class);
		Assertions.assertEquals("classpath_config.properties", loadFromClasspath.getConfigName());
	}
	
	@Test
	void test_PropertyFileLoader()
	{
		LoadFromFile loadFromFile = getSingleConfigInstance(LoadFromFile.class);
		Assertions.assertEquals("workdir_config.properties", loadFromFile.getConfigName());
	}
	
	@Test
	void test_HttpLoader()
	{
		LoadFromHttp loadFromHttp = getSingleConfigInstance(LoadFromHttp.class);
		Assertions.assertEquals("http_config.properties", loadFromHttp.getConfigName());
	}
	
	@Disabled
	@Test
	void test_ConfigLinkerLoader()
	{
		Assertions.fail("Not implemented.");
	}
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "./configs/workdir_config.properties")
interface LoadFromFile
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "classpath_config.properties", sourceScheme = BoundObject.SourceScheme.CLASSPATH)
interface LoadFromClasspath
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "http://localhost:5050/configs/http_config.properties", sourceScheme = BoundObject.SourceScheme.HTTP)
interface LoadFromHttp
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "<ConfigLinker.server>", sourceScheme = BoundObject.SourceScheme.CONFIG_LINKER_SERVER)
interface LoadFromConfigLinkerServer
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
