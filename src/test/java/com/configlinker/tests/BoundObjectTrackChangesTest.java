package com.configlinker.tests;

import com.configlinker.ConfigChangedEvent;
import com.configlinker.IConfigChangeListener;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.SourceScheme;
import com.configlinker.enums.TrackPolicy;
import com.configlinker.tests.httpserver.DownloadFileHandler;
import com.configlinker.tests.httpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundObjectTrackChangesTest extends AbstractBaseTest
{
	private static final Path templatePropertyFilePath = Paths.get("./configs/track_changes.template.properties");
	private static final String nameKey = "change.name";
	private static final String originalName = "John";
	private static final String newName = "Joe";
	private static final String surnameKey = "change.surname";
	private static final String originalSurname = "Kirigaya";
	private static final String newSurname = "Black";
	
	@Test
	void someTest()
	{
		// trackPolicy
		// trackInterval
		// changeListener
		//FactorySettingsBuilder.create().setListener
	}
	
	private void changeProperties(Path filePath) throws IOException
	{
		try (InputStream propFileIS = Files.newInputStream(filePath);
		     OutputStream propFileOS = Files.newOutputStream(filePath))
		{
			Properties fileProp = new Properties();
			fileProp.load(propFileIS);
			fileProp.put(nameKey, newName);
			fileProp.put(surnameKey, newSurname);
			fileProp.store(propFileOS, "Modified");
		}
	}
	
	@Test
	void test_trackFileChanges() throws InterruptedException, IOException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
			Files.copy(templatePropertyFilePath, trackFilePath);
			TrackFileChanges trackFileChanges = getSingleConfigInstance(TrackFileChanges.class);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			changeProperties(trackFilePath);
			Thread.sleep(5000);
			Assertions.assertEquals(newName, trackFileChanges.name());
			Assertions.assertEquals(newSurname, trackFileChanges.surname());
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException ignore)
				{
					ignore.printStackTrace();
				}
			}
		}
	}
	
	@Test @Disabled
	void test_trackFileClasspathChanges()
	{
	
	}
	
	@Test @Disabled
	void test_trackHttpChanges()
	{
	
	}
	
	@Test @Disabled
	void test_trackHttpChangesWithCustomInterval()
	{
	
	}
	
	@Test @Disabled
	void test_trackFileChangesAndListener()
	{
	
	}
}

@BoundObject(sourcePath = "./configs/track_changes.file.properties", trackingPolicy = TrackPolicy.ENABLE)
interface TrackFileChanges
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

@BoundObject(sourceScheme = SourceScheme.CLASSPATH, sourcePath = "./configs/track_changes.classpath.properties", trackingPolicy = TrackPolicy.ENABLE)
interface TrackFileClasspathChanges
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

@BoundObject(sourceScheme = SourceScheme.HTTP, sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "track_changes.http.properties", trackingPolicy = TrackPolicy.ENABLE)
interface TrackHttpChanges
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

@BoundObject(sourceScheme = SourceScheme.HTTP, sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "track_changes.http.properties",
	trackingPolicy = TrackPolicy.ENABLE, trackingInterval = 15)
interface TrackHttpChangesWithInterval
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

class MyConfigChangeListener implements IConfigChangeListener
{
	@Override
	public void configChanged(ConfigChangedEvent configChangedEvent)
	{
		// TODO: implement
	}
}

@BoundObject(sourceScheme = SourceScheme.HTTP, sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "track_changes.http.properties",
	trackingPolicy = TrackPolicy.ENABLE, trackingInterval = 15, changeListener = MyConfigChangeListener.class)
interface TrackHttpChangesWithIntervalAndChangeListener
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}
