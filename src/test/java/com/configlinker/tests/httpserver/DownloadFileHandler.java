package com.configlinker.tests.httpserver;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DownloadFileHandler implements HttpHandler
{
	public static final String PATH = "/configs/";
	public static final String default_folder = "./configs";
	
	private final Path folderPath;
	
	public DownloadFileHandler()
	{
		this(default_folder);
	}
	
	public DownloadFileHandler(String folderPath)
	{
		this.folderPath = Paths.get(folderPath).normalize().toAbsolutePath();
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException
	{
		String fullPath = httpExchange.getRequestURI().getPath();
		String fileName = fullPath.substring(PATH.length());
		Path filePath = folderPath.resolve(fileName);
		
		// TODO: log request info to the file in 'test_workdir/logs' directory
		// 1-line: fileName
		// 2-line: headers (one by line)
		// n-line: blank
		// n+1 line: another request data
		
		httpExchange.getResponseHeaders().add("Content-Type", "text/plain");
		
		if (!Files.exists(filePath))
		{
			byte[] msg = ("File '" + fileName + "' not found.").getBytes();
			httpExchange.sendResponseHeaders(404, msg.length);
			httpExchange.getResponseBody().write(msg);
		}
		else
		{
			byte[] content = Files.readAllBytes(filePath);
			httpExchange.sendResponseHeaders(200, content.length);
			httpExchange.getResponseBody().write(content);
			
		}
		
		httpExchange.getRequestBody().close();
		httpExchange.getResponseBody().close();
	}
}
