package com.configlinker.tests;

import com.configlinker.ConfigChangedEvent;
import com.configlinker.IConfigChangeListener;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.ErrorBehavior;
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
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
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
	
	
	private void changeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (InputStream propFileIS = Files.newInputStream(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileIS);
		}
		
		try (OutputStream propFileOS = Files.newOutputStream(filePath, StandardOpenOption.WRITE))
		{
			fileProp.put(nameKey, newName);
			fileProp.put(surnameKey, newSurname);
			fileProp.store(propFileOS, "Modified");
		}
	}
	
	private void partiallyChangeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (InputStream propFileIS = Files.newInputStream(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileIS);
		}
		
		try (OutputStream propFileOS = Files.newOutputStream(filePath, StandardOpenOption.WRITE))
		{
			fileProp.put(nameKey, newName);
			fileProp.store(propFileOS, "Modified");
		}
	}
	
	private void partiallyRemoveAndChangeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (InputStream propFileIS = Files.newInputStream(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileIS);
		}
		
		try (OutputStream propFileOS = Files.newOutputStream(filePath, StandardOpenOption.WRITE))
		{
			fileProp.remove(nameKey);
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
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileChanges trackFileChanges = getSingleConfigInstance(TrackFileChanges.class);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			changeProperties(trackFilePath);
			Thread.sleep(10000);
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
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackFilePartialChanges() throws InterruptedException, IOException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileChanges trackFileChanges = getSingleConfigInstance(TrackFileChanges.class);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackFilePartialChangesWithThrowBehaviour() throws InterruptedException, IOException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileChanges trackFileChanges = getSingleConfigInstance(TrackFileChanges.class);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			partiallyRemoveAndChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackFilePartialChangesWithNullBehaviour() throws InterruptedException, IOException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileChangesNullBehaviour trackFileChangesNullBehaviour = getSingleConfigInstance(TrackFileChangesNullBehaviour.class);
			Assertions.assertEquals(originalName, trackFileChangesNullBehaviour.name());
			Assertions.assertEquals(originalSurname, trackFileChangesNullBehaviour.surname());
			partiallyRemoveAndChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertNull(trackFileChangesNullBehaviour.name());
			Assertions.assertEquals(newSurname, trackFileChangesNullBehaviour.surname());
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackFileClasspathChanges() throws IOException, InterruptedException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = Paths.get(BoundObjectTrackChangesTest.class.getClassLoader().getResource(".").getPath(), "track_changes.classpath.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileClasspathChanges trackFileClasspathChanges = getSingleConfigInstance(TrackFileClasspathChanges.class);
			Assertions.assertEquals(originalName, trackFileClasspathChanges.name());
			Assertions.assertEquals(originalSurname, trackFileClasspathChanges.surname());
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileClasspathChanges.name());
			Assertions.assertEquals(originalSurname, trackFileClasspathChanges.surname());
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackHttpChanges() throws IOException, InterruptedException
	{
		Path trackFilePath = null;
		
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.http.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			TrackHttpChanges trackHttpChanges = getSingleConfigInstance(TrackHttpChanges.class);
			Assertions.assertEquals(originalName, trackHttpChanges.name());
			Assertions.assertEquals(originalSurname, trackHttpChanges.surname());
			changeProperties(trackFilePath);
			Thread.sleep(61000);
			
			Assertions.assertEquals(newName, trackHttpChanges.name());
			Assertions.assertEquals(newSurname, trackHttpChanges.surname());
		}
		finally
		{
			SimpleHttpServer.shutdown();
			
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackHttpChangesWithCustomInterval() throws InterruptedException, IOException
	{
		Path trackFilePath = null;
		
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.http-interval.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			TrackHttpChangesWithInterval trackHttpChangesWithInterval = getSingleConfigInstance(TrackHttpChangesWithInterval.class);
			Assertions.assertEquals(originalName, trackHttpChangesWithInterval.name());
			Assertions.assertEquals(originalSurname, trackHttpChangesWithInterval.surname());
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(16000);
			
			Assertions.assertEquals(newName, trackHttpChangesWithInterval.name());
			Assertions.assertEquals(originalSurname, trackHttpChangesWithInterval.surname());
		}
		finally
		{
			SimpleHttpServer.shutdown();
			
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	void test_trackFileChangesAndListener() throws IOException, InterruptedException
	{
		Path trackFilePath = null;
		try
		{
			trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes_listener.file.properties");
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			TrackFileChangesWithChangeListener trackFileChangesWithChangeListener = getSingleConfigInstance(TrackFileChangesWithChangeListener.class);
			Assertions.assertEquals(originalName, trackFileChangesWithChangeListener.name());
			Assertions.assertEquals(originalSurname, trackFileChangesWithChangeListener.surname());
			changeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileChangesWithChangeListener.name());
			Assertions.assertEquals(newSurname, trackFileChangesWithChangeListener.surname());
			Assertions.assertTrue(MyConfigChangeListener.wasCalled(), "MyConfigChangeListener was called");
		}
		finally
		{
			if (trackFilePath != null)
			{
				try
				{
					Files.deleteIfExists(trackFilePath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	// TODO: FactorySettingsBuilder.create().setListener -- set global listener
}

@BoundObject(sourcePath = "./configs/track_changes.file.properties", trackingPolicy = TrackPolicy.ENABLE)
interface TrackFileChanges
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

@BoundObject(sourcePath = "./configs/track_changes.file.properties", trackingPolicy = TrackPolicy.ENABLE)
interface TrackFileChangesNullBehaviour
{
	@BoundProperty(name = "change.name", errorBehavior = ErrorBehavior.RETURN_NULL)
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}

@BoundObject(sourceScheme = SourceScheme.CLASSPATH, sourcePath = "./track_changes.classpath.properties", trackingPolicy = TrackPolicy.ENABLE)
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

@BoundObject(sourceScheme = SourceScheme.HTTP, sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "track_changes.http-interval.properties",
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
	private static boolean wasCalled = false;
	
	static boolean wasCalled()
	{
		return wasCalled;
	}
	
	@Override
	public void configChanged(ConfigChangedEvent configChangedEvent)
	{
		Assertions.assertNull(configChangedEvent.getException());
		Assertions.assertEquals(TrackFileChangesWithChangeListener.class, configChangedEvent.getConfigInterface() );
		Assertions.assertEquals("./configs/track_changes_listener.file.properties", configChangedEvent.getSourcePath() );
		Map<String, ConfigChangedEvent.ValuesPair> rawValues = configChangedEvent.getRawValues();
	}
}

@BoundObject(sourcePath = "./configs/track_changes_listener.file.properties", trackingPolicy = TrackPolicy.ENABLE, changeListener = MyConfigChangeListener.class)
interface TrackFileChangesWithChangeListener
{
	@BoundProperty(name = "change.name")
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}
