package com.configlinker.tests.httpserver;


import com.configlinker.tests.httpserver.SimpleHttpServer.RequestCallbackListener;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class DownloadFileHandler implements HttpHandler
{
	public static final String PATH = "/configs/";
	public static final String DEFAULT_FOLDER = "./configs";
	
	private final Path folderPath;
	private final RequestCallbackListener callback;
	
	
	DownloadFileHandler(RequestCallbackListener callback)
	{
		this(DEFAULT_FOLDER, callback);
	}
	
	DownloadFileHandler(String folderPath, RequestCallbackListener callback)
	{
		this.folderPath = Paths.get(folderPath).normalize().toAbsolutePath();
		this.callback = callback;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException
	{
		String fullPath = httpExchange.getRequestURI().getPath();
		String fileName = fullPath.substring(PATH.length());
		Path filePath = folderPath.resolve(fileName);
		
		if (callback != null)
		{
			HashMap<String, String> requestData = new HashMap<>();
			requestData.put(RequestCallbackListener.RequestURIPath, fullPath);
			requestData.put(RequestCallbackListener.FilePath, filePath.toString());
			requestData.put(RequestCallbackListener.FileName, fileName);
			
			for (Map.Entry<String, java.util.List<String>> header : httpExchange.getRequestHeaders().entrySet())
			{
				requestData.put(header.getKey(), header.getValue().get(0));
			}
			
			callback.afterRequestReceived(requestData);
		}
		
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
