package com.configlinker.tests.httpserver;


import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SimpleHttpServer
{
	public static final String hostName = "localhost";
	public static final int port = 7070;
	
	private static final int stopDelay = 2;
	
	private static HttpServer httpServer;
	
	
	private SimpleHttpServer() { }
	
	public static void prepare()
	{
		try
		{
			httpServer = HttpServer.create();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// create contexts with mapping to path
		HttpContext getTaskContext = httpServer.createContext(DownloadFileHandler.PATH, new DownloadFileHandler());
		
		// set executor to server
		Executor executor = Executors.newFixedThreadPool(3, new SimpleThreadFactory(SimpleHttpServer.class.getSimpleName(), true));
		httpServer.setExecutor(executor);
		
		try
		{
			httpServer.bind(new InetSocketAddress(hostName, port), 5);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void start()
	{
		if (httpServer != null)
			httpServer.start();
	}
	
	public static void shutdown()
	{
		if (httpServer != null)
			httpServer.stop(stopDelay);
	}
}
