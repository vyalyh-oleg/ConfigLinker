package net.crispcode.configlinker.tests;

import net.crispcode.configlinker.ConfigChangedEvent;
import net.crispcode.configlinker.ConfigSet;
import net.crispcode.configlinker.IConfigChangeListener;
import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.annotations.BoundProperty;
import net.crispcode.configlinker.enums.ErrorBehavior;
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.enums.TrackPolicy;
import net.crispcode.configlinker.exceptions.ConfigLinkerRuntimeException;
import net.crispcode.configlinker.exceptions.PropertyNotFoundException;
import net.crispcode.configlinker.tests.httpserver.DownloadFileHandler;
import net.crispcode.configlinker.tests.httpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Properties;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class BoundObjectTrackChangesTest extends AbstractBaseTest
{
	private static final Path templatePropertyFilePath = Paths.get("./configs/track_changes.template.properties");
	static final String nameKey = "change.name";
	static final String originalName = "John";
	static final String newName = "Joe";
	static final String surnameKey = "change.surname";
	static final String originalSurname = "Kirigaya";
	static final String newSurname = "Black";
	
	
	private void changeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (BufferedReader propFileReader = Files.newBufferedReader(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileReader);
		}
		
		try (BufferedWriter propFileWriter = Files.newBufferedWriter(filePath))
		{
			fileProp.put(nameKey, newName);
			fileProp.put(surnameKey, newSurname);
			fileProp.store(propFileWriter, "Modified");
		}
	}
	
	private void partiallyChangeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (BufferedReader propFileReader = Files.newBufferedReader(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileReader);
		}
		
		try (BufferedWriter propFileWriter = Files.newBufferedWriter(filePath))
		{
			fileProp.put(nameKey, newName);
			fileProp.store(propFileWriter, "Modified");
		}
	}
	
	private void partiallyRemoveAndChangeProperties(Path filePath) throws IOException
	{
		Properties fileProp;
		try (BufferedReader propFileReader = Files.newBufferedReader(filePath))
		{
			fileProp = new Properties();
			fileProp.load(propFileReader);
		}
		
		try (BufferedWriter propFileWriter = Files.newBufferedWriter(filePath))
		{
			fileProp.remove(nameKey);
			fileProp.put(surnameKey, newSurname);
			fileProp.store(propFileWriter, "Modified");
		}
	}
	
	
	@Test
	void test_trackFileChanges() throws InterruptedException, IOException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChanges.class);
			TrackFileChanges trackFileChanges = configSet.getConfigObject(TrackFileChanges.class);
			
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			changeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileChanges.name());
			Assertions.assertEquals(newSurname, trackFileChanges.surname());
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackFilePartialChanges() throws InterruptedException, IOException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChanges.class);
			TrackFileChanges trackFileChanges = configSet.getConfigObject(TrackFileChanges.class);
			
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	@Test
	void test_trackFilePartialChangesWithThrowBehaviour() throws InterruptedException, IOException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChangesThrowBehaviourWithChangeListener.class);
			TrackFileChangesThrowBehaviourWithChangeListener trackFileChanges = configSet.getConfigObject(TrackFileChangesThrowBehaviourWithChangeListener.class);
			
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			partiallyRemoveAndChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(originalName, trackFileChanges.name());
			Assertions.assertEquals(originalSurname, trackFileChanges.surname());
			
			Assertions.assertTrue(MyConfigChangeThrowBehaviourListener.wasCalled(), "MyConfigChangeThrowBehaviourListener wasn't called or didn't pass the assertions.");
		}
		finally
		{
			if (configSet != null)
				configSet.stopTrackChanges();
			
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
	
	@Test
	void test_trackFilePartialChangesWithNullBehaviour() throws InterruptedException, IOException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChangesNullBehaviour.class);
			TrackFileChangesNullBehaviour trackFileChangesNullBehaviour = configSet.getConfigObject(TrackFileChangesNullBehaviour.class);
			
			Assertions.assertEquals(originalName, trackFileChangesNullBehaviour.name());
			Assertions.assertEquals(originalSurname, trackFileChangesNullBehaviour.surname());
			partiallyRemoveAndChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertNull(trackFileChangesNullBehaviour.name());
			Assertions.assertEquals(newSurname, trackFileChangesNullBehaviour.surname());
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackFileClasspathChanges() throws IOException, InterruptedException
	{
		Path trackFilePath = Paths.get(BoundObjectTrackChangesTest.class.getClassLoader().getResource(".").getPath(), "track_changes.classpath.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileClasspathChanges.class);
			TrackFileClasspathChanges trackFileClasspathChanges = configSet.getConfigObject(TrackFileClasspathChanges.class);
			
			Assertions.assertEquals(originalName, trackFileClasspathChanges.name());
			Assertions.assertEquals(originalSurname, trackFileClasspathChanges.surname());
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileClasspathChanges.name());
			Assertions.assertEquals(originalSurname, trackFileClasspathChanges.surname());
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackHttpChanges() throws IOException, InterruptedException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.http.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			configSet = getConfigSet(TrackHttpChanges.class);
			TrackHttpChanges trackHttpChanges = configSet.getConfigObject(TrackHttpChanges.class);
			
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
			
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackHttpChangesWithCustomInterval() throws InterruptedException, IOException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes.http-interval.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			SimpleHttpServer.prepare();
			SimpleHttpServer.start();
			Thread.sleep(1000);
			
			configSet = getConfigSet(TrackHttpChangesWithInterval.class);
			TrackHttpChangesWithInterval trackHttpChangesWithInterval = configSet.getConfigObject(TrackHttpChangesWithInterval.class);
			
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
			
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackFileChangesAndCallListener() throws IOException, InterruptedException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes_listener.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChangesWithChangeListener.class);
			TrackFileChangesWithChangeListener trackFileChangesWithChangeListener = configSet.getConfigObject(TrackFileChangesWithChangeListener.class);
			
			Assertions.assertEquals(originalName, trackFileChangesWithChangeListener.name());
			Assertions.assertEquals(originalSurname, trackFileChangesWithChangeListener.surname());
			changeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertEquals(newName, trackFileChangesWithChangeListener.name());
			Assertions.assertEquals(newSurname, trackFileChangesWithChangeListener.surname());
			
			Assertions.assertTrue(MyConfigChangeListener.wasCalled(), "MyConfigChangeListener wasn't called or didn't pass the assertions.");
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Test
	void test_trackFileChangesNullBehaviourAndCallListener() throws IOException, InterruptedException
	{
		Path trackFilePath = templatePropertyFilePath.getParent().resolve("track_changes_nullbehaviour_listener.file.properties");
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(templatePropertyFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(TrackFileChangesNullBehaviourWithChangeListener.class);
			TrackFileChangesNullBehaviourWithChangeListener trackFileChangesNBWithChangeListener = configSet.getConfigObject(TrackFileChangesNullBehaviourWithChangeListener.class);
			
			Assertions.assertEquals(originalName, trackFileChangesNBWithChangeListener.name());
			Assertions.assertEquals(originalSurname, trackFileChangesNBWithChangeListener.surname());
			partiallyRemoveAndChangeProperties(trackFilePath);
			Thread.sleep(10000);
			Assertions.assertNull(trackFileChangesNBWithChangeListener.name());
			Assertions.assertEquals(newSurname, trackFileChangesNBWithChangeListener.surname());
			
			Assertions.assertTrue(MyConfigChangeNullBehaviourListener.wasCalled(), "MyConfigChangeNullBehaviourListener wasn't called or didn't pass the assertions.");
		}
		finally
		{
			try
			{
				if (configSet != null)
					configSet.stopTrackChanges();
				
				Files.deleteIfExists(trackFilePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
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

class MyConfigChangeThrowBehaviourListener implements IConfigChangeListener
{
	private static boolean wasCalled = false;
	
	static boolean wasCalled()
	{
		return wasCalled;
	}
	
	@Override
	public void configChanged(ConfigChangedEvent configChangedEvent)
	{
		Assertions.assertEquals(TrackFileChangesThrowBehaviourWithChangeListener.class, configChangedEvent.getConfigInterface());
		Assertions.assertEquals("./configs/track_changes.file.properties", configChangedEvent.getSourcePath());
		
		ConfigLinkerRuntimeException exception = configChangedEvent.getException();
		Assertions.assertEquals(PropertyNotFoundException.class, exception.getClass());
		Assertions.assertEquals("Value for property 'change.name' not found, config interface 'net.crispcode.configlinker.tests.TrackFileChangesThrowBehaviourWithChangeListener#name'.", exception.getMessage());
		
		Map<String, ConfigChangedEvent.ValuesPair> rawValues = configChangedEvent.getRawValues();
		Assertions.assertEquals(2, rawValues.size());
		
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalName, rawValues.get(BoundObjectTrackChangesTest.nameKey).getOldValue());
		Assertions.assertNull(rawValues.get(BoundObjectTrackChangesTest.nameKey).getNewValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getOldValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.newSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getNewValue());
		
		wasCalled = true;
	}
}

@BoundObject(sourcePath = "./configs/track_changes.file.properties", trackingPolicy = TrackPolicy.ENABLE, changeListener = MyConfigChangeThrowBehaviourListener.class)
interface TrackFileChangesThrowBehaviourWithChangeListener
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
		Assertions.assertNull(configChangedEvent.getException(), "Exception should be null.");
		Assertions.assertEquals(TrackFileChangesWithChangeListener.class, configChangedEvent.getConfigInterface());
		Assertions.assertEquals("./configs/track_changes_listener.file.properties", configChangedEvent.getSourcePath());
		
		Map<String, ConfigChangedEvent.ValuesPair> rawValues = configChangedEvent.getRawValues();
		Assertions.assertEquals(2, rawValues.size());
		
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalName, rawValues.get(BoundObjectTrackChangesTest.nameKey).getOldValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.newName, rawValues.get(BoundObjectTrackChangesTest.nameKey).getNewValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getOldValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.newSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getNewValue());
		
		wasCalled = true;
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

class MyConfigChangeNullBehaviourListener implements IConfigChangeListener
{
	private static boolean wasCalled = false;
	
	static boolean wasCalled()
	{
		return wasCalled;
	}
	
	@Override
	public void configChanged(ConfigChangedEvent configChangedEvent)
	{
		Assertions.assertNull(configChangedEvent.getException(), "Exception should be null.");
		Assertions.assertEquals(TrackFileChangesNullBehaviourWithChangeListener.class, configChangedEvent.getConfigInterface());
		Assertions.assertEquals("./configs/track_changes_nullbehaviour_listener.file.properties", configChangedEvent.getSourcePath());
		
		Map<String, ConfigChangedEvent.ValuesPair> rawValues = configChangedEvent.getRawValues();
		Assertions.assertEquals(2, rawValues.size());
		
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalName, rawValues.get(BoundObjectTrackChangesTest.nameKey).getOldValue());
		Assertions.assertNull(rawValues.get(BoundObjectTrackChangesTest.nameKey).getNewValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.originalSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getOldValue());
		Assertions.assertEquals(BoundObjectTrackChangesTest.newSurname, rawValues.get(BoundObjectTrackChangesTest.surnameKey).getNewValue());
		
		wasCalled = true;
	}
}

@BoundObject(sourcePath = "./configs/track_changes_nullbehaviour_listener.file.properties", trackingPolicy = TrackPolicy.ENABLE, changeListener = MyConfigChangeNullBehaviourListener.class)
interface TrackFileChangesNullBehaviourWithChangeListener
{
	@BoundProperty(name = "change.name", errorBehavior = ErrorBehavior.RETURN_NULL)
	String name();
	
	@BoundProperty(name = "change.surname")
	String surname();
}
