package com.configlinker.tests;

import com.configlinker.FactorySettingsBuilder;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.SourceScheme;
import com.configlinker.tests.httpserver.DownloadFileHandler;
import com.configlinker.tests.httpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


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
	
	@Test
	void test_HttpLoader_WithFactoryHeaders() throws InterruptedException
	{
		// predefined headers
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorize-Key", "my-secret-key--AA-bB-Cc");
		headers.put("Server-Id", "1234567890");
		
		Map<String, String>[] request = new Map[1];
		SimpleHttpServer.RequestCallbackListener callbackListener = new SimpleHttpServer.RequestCallbackListener()
		{
			@Override
			public void afterRequestReceived(Map<String, String> requestData)
			{
				request[0] = requestData;
			}
			
			@Override
			public void beforeResponseSend(Map<String, String> responseData)
			{
			}
		};
		
		try
		{
			SimpleHttpServer.prepare(callbackListener);
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			FactorySettingsBuilder fsb = FactorySettingsBuilder.create().setHttpHeaders(headers);
			LoadFromHttp loadFromHttp = getSingleConfigInstance(fsb, LoadFromHttp.class);
			Assertions.assertEquals("value from http_config.properties", loadFromHttp.getConfigName());
			
			// check request headers
			Thread.sleep(500);
			Assertions.assertEquals("my-secret-key--AA-bB-Cc", request[0].get("Authorize-Key".toLowerCase()));
			Assertions.assertEquals("1234567890", request[0].get("Server-Id".toLowerCase()));
		}
		finally
		{
			SimpleHttpServer.shutdown();
		}
	}
	
	@Test
	void test_HttpLoader_WithFactoryHeaders_WithBoundObjectHeaders() throws InterruptedException
	{
		// predefined headers
		HashMap<String, String> headers = new HashMap<>();
		headers.put("authorize-key", "my-secret-key--DD-ee-FF");
		headers.put("Server-Id", "1234567890_11");
		
		Map<String, String>[] request = new Map[1];
		SimpleHttpServer.RequestCallbackListener callbackListener = new SimpleHttpServer.RequestCallbackListener()
		{
			@Override
			public void afterRequestReceived(Map<String, String> requestData)
			{
				request[0] = requestData;
			}
			
			@Override
			public void beforeResponseSend(Map<String, String> responseData)
			{
			
			}
		};
		
		try
		{
			SimpleHttpServer.prepare(callbackListener);
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			FactorySettingsBuilder fsb = FactorySettingsBuilder.create().setHttpHeaders(headers);
			LoadFromHttpUsingHeaders loadFromHttp = getSingleConfigInstance(fsb, LoadFromHttpUsingHeaders.class);
			Assertions.assertEquals("value from http_config.properties", loadFromHttp.getConfigName());
			
			// check request headers
			Thread.sleep(500);
			Assertions.assertEquals("My-Secret-Key--zz-yy-xx", request[0].get("Authorize-Key".toLowerCase()));
			Assertions.assertEquals("1234567890_11", request[0].get("Server-Id".toLowerCase()));
			Assertions.assertEquals("0987654321_00", request[0].get("Client-Id".toLowerCase()));
		}
		finally
		{
			SimpleHttpServer.shutdown();
		}
	}
	
	// TODO: implement test_ConfigLinkerLoader
	@Test
	@Disabled("TODO: implement")
	void test_ConfigLinkerLoader()
	{
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
	httpHeaders = {"Authorize-Key: My-Secret-Key--zz-yy-xx", "Client-Id: 0987654321_00"})
interface LoadFromHttpUsingHeaders
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
