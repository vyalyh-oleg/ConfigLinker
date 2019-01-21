package com.configlinker.tests;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.SourceScheme;
import com.configlinker.tests.httpserver.DownloadFileHandler;
import com.configlinker.tests.httpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


class LoadersTest extends AbstractBaseTest
{
	@Test
	void test_ClasspathLoader()
	{
		LoadFromClasspath loadFromClasspath = getSingleConfigInstance(LoadFromClasspath.class);
		Assertions.assertEquals("value from classpath_config.properties", loadFromClasspath.getConfigName());
	}
	
	@Test
	void test_PropertyFileLoader()
	{
		LoadFromFile loadFromFile = getSingleConfigInstance(LoadFromFile.class);
		Assertions.assertEquals("value from workdir_config.properties", loadFromFile.getConfigName());
	}
	
	@Test
	void test_HttpLoader() throws InterruptedException
	{
		try
		{
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			LoadFromHttp loadFromHttp = getSingleConfigInstance(LoadFromHttp.class);
			Assertions.assertEquals("value from http_config.properties", loadFromHttp.getConfigName());
		}
		finally
		{
			SimpleHttpServer.shutdown();
		}
	}
	
	// httpHeaders
	@Disabled("TODO: implement")
	@Test
	void test_HttpLoaderWithCustomHeaders() throws InterruptedException
	{
		// TODO: implement test_HttpLoaderWithCustomHeaders
		try
		{
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			LoadFromHttp loadFromHttp = getSingleConfigInstance(LoadFromHttp.class);
			Assertions.assertEquals("value from classpath_config.properties", loadFromHttp.getConfigName());
			
			// check headers from log file in 'test_workdir/logs'
		}
		finally
		{
			// delete all log files
			SimpleHttpServer.shutdown();
		}
	}
	
	@Test @Disabled("TODO: implement")
	void test_ConfigLinkerLoader()
	{
		// TODO: implement test_ConfigLinkerLoader
		Assertions.fail("test_ConfigLinkerLoader not implemented.");
	}
}

// --------------------------------------------------------------------------------

@BoundObject(sourcePath = "./configs/workdir_config.properties")
interface LoadFromFile
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "classpath_config.properties", sourceScheme = SourceScheme.CLASSPATH)
interface LoadFromClasspath
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "http_config.properties", sourceScheme = SourceScheme.HTTP)
interface LoadFromHttp
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "http_config.properties", sourceScheme = SourceScheme.HTTP,
	httpHeaders = {"Authorize:<my-secret-key>", "Server-Id: 1234567890"})
interface LoadFromHttpWithHeaders
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}

@BoundObject(sourcePath = "<ConfigLinker.server>", sourceScheme = SourceScheme.CONFIG_LINKER_SERVER)
interface LoadFromConfigLinkerServer
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
