package net.crispcode.configlinker.tests;


import net.crispcode.configlinker.ConfigChangedEvent;
import net.crispcode.configlinker.ConfigSet;
import net.crispcode.configlinker.FactorySettingsBuilder;
import net.crispcode.configlinker.IConfigChangeListener;
import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.annotations.BoundProperty;
import net.crispcode.configlinker.deserializers.DateType;
import net.crispcode.configlinker.enums.SourceScheme;
import net.crispcode.configlinker.enums.TrackPolicy;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PropertyVariableSubstitutionTest extends AbstractBaseTest
{
	@Test
	void test_valueWithPrefix()
	{
		Prop_ValueWithPrefix properties = getSingleConfigInstance(Prop_ValueWithPrefix.class);
		Assertions.assertEquals(1, properties.javaPriority());
	}
	
	@Test
	void test_valueWithAndWithoutPrefix()
	{
		Prop_ValueWithAndWithoutPrefix properties = getSingleConfigInstance(Prop_ValueWithAndWithoutPrefix.class);
		Assertions.assertEquals(2, properties.cPriority());
		Assertions.assertEquals("Dennis Ritchie & Bell Labs", properties.cAuthor());
	}
	
	@Test
	void test_sourcePathWithVar()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create().addParameter("subfolder", "configs");
		Prop_SourcePathWithVar properties = getSingleConfigInstance(builder, Prop_SourcePathWithVar.class);
		Assertions.assertEquals(Arrays.asList(Lang.values()), properties.languages());
	}
	
	@Test
	void test_prefixWithVar()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create().addParameter("part", "languages");
		Prop_PrefixWithVar properties = getSingleConfigInstance(builder, Prop_PrefixWithVar.class);
		Assertions.assertEquals(4, properties.pythonPriority());
	}
	
	@Test
	void test_valueWithVar()
	{
		FactorySettingsBuilder builderCpp = FactorySettingsBuilder.create().addParameter("lang", "Cpp");
		Prop_ValueWithVar propertiesCpp = getSingleConfigInstance(builderCpp, Prop_ValueWithVar.class);
		Assertions.assertEquals("Bjarne Stroustrup", propertiesCpp.langAuthor());
		
		FactorySettingsBuilder builderCsh = FactorySettingsBuilder.create().addParameter("lang", "Csh");
		Prop_ValueWithVar propertiesCsh = getSingleConfigInstance(builderCsh, Prop_ValueWithVar.class);
		Assertions.assertEquals("Microsoft", propertiesCsh.langAuthor());
		
		FactorySettingsBuilder builderJS = FactorySettingsBuilder.create().addParameter("lang", "JavaScript");
		Prop_ValueWithVar propertiesJS = getSingleConfigInstance(builderJS, Prop_ValueWithVar.class);
		Assertions.assertEquals("Brendan Eich", propertiesJS.langAuthor());
	}
	
	@Test
	void test_sourcePathWithVar_PrefixWithVar_ValuesWithVar_ValueWithAndWithoutPrefix()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "configs")
			.addParameter("part", "languages")
			.addParameter("lang", "PHP");
		Prop_VarInAllParts properties = getSingleConfigInstance(builder, Prop_VarInAllParts.class);
		
		Assertions.assertEquals(6, properties.langPriority());
		Assertions.assertEquals(Date.from(LocalDate.of(1995, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), properties.langBirthday());
	}
	
	@Test
	void test_dynamicPropString_VarInAllParts()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "configs")
			.addParameter("globalPrefix", "programming")
			.addParameter("part", "languages");
		DynamicPropString_VarInAllParts properties = getSingleConfigInstance(builder, DynamicPropString_VarInAllParts.class);
		
		Assertions.assertEquals("Microsoft", properties.langAuthor("Csh"));
		Assertions.assertEquals("Brendan Eich", properties.langAuthor("JavaScript"));
		Assertions.assertEquals("1991-02-20", properties.langInfo("Python", "birthday"));
		Assertions.assertEquals("7", properties.langInfo("JavaScript", "priority"));
	}
	
	@Test
	void test_dynamicPropStringEnum_VarInAllParts()
	{
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", "configs")
			.addParameter("globalPrefix", "programming")
			.addParameter("part", "languages");
		DynamicPropStringEnum_VarInAllParts properties = getSingleConfigInstance(builder, DynamicPropStringEnum_VarInAllParts.class);
		
		Assertions.assertEquals("7", properties.langInfo(Lang.JavaScript, Info.priority));
		Assertions.assertEquals("Zend Technologies", properties.langInfo(Lang.PHP, Info.designed));
		Assertions.assertEquals("1972-01-01", properties.langInfo(Lang.C, Info.birthday));
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
			fileProp.put("programming.languages.Java.priority", "0");
			fileProp.put("programming.languages.Python.designed", "Guido");
			fileProp.put("programming.languages.Csh.birthday", "2000-01");
			fileProp.put("programming.paradigm.declarative", "final result");
			
			fileProp.store(propFileWriter, "Modified");
		}
	}
	
	@Test
	void test_dynamicProp_VarInAllParts_TrackChanges() throws IOException, InterruptedException
	{
		final ArrayList<String> paradigmsFromConfig = new ArrayList<>();
		paradigmsFromConfig.add("functional");
		paradigmsFromConfig.add("logic");
		
		final String subfolder = "configs";
		final String configName = "variable_substitution.track_changes.properties";
		FactorySettingsBuilder builder = FactorySettingsBuilder.create()
			.addParameter("subfolder", subfolder)
			.addParameter("configName", configName)
			.addParameter("globalPrefix", "programming")
			.addParameter("part", "languages");
		
		Path originalFilePath = Paths.get("./configs/variable_substitution.properties");
		Path trackFilePath = Paths.get(subfolder, configName);
		ConfigSet configSet = null;
		
		try
		{
			Files.copy(originalFilePath, trackFilePath, StandardCopyOption.REPLACE_EXISTING);
			configSet = getConfigSet(builder, DynamicProp_VarInAllParts_TrackChanges.class);
			DynamicProp_VarInAllParts_TrackChanges properties = configSet.getConfigObject(DynamicProp_VarInAllParts_TrackChanges.class);
			
			Assertions.assertEquals(paradigmsFromConfig, properties.declarativeParadigms());
			Assertions.assertEquals("1", properties.langInfo(Lang.Java, Info.priority));
			Assertions.assertEquals("Guido van Rossum", properties.langInfo(Lang.Python, Info.designed));
			Assertions.assertEquals("2000-01-01", properties.langInfo(Lang.Csh, Info.birthday));
			
			partiallyChangeProperties(trackFilePath);
			Thread.sleep(10000);
			
			final ArrayList<String> changedParadigms = new ArrayList<>();
			changedParadigms.add("final result");
			
			Assertions.assertEquals(changedParadigms, properties.declarativeParadigms());
			Assertions.assertEquals("0", properties.langInfo(Lang.Java, Info.priority));
			Assertions.assertEquals("Guido", properties.langInfo(Lang.Python, Info.designed));
			Assertions.assertEquals("2000-01", properties.langInfo(Lang.Csh, Info.birthday));
			
			Assertions.assertTrue(DynamicPropConfigChangeListener.wasCalled(), "DynamicPropConfigChangeListener wasn't called or didn't pass the assertions.");
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
	void test_HttpLoader_WithFactoryHeadersAndBoundObjectHeaders_WithVar() throws InterruptedException
	{
		// predefined headers
		HashMap<String, String> headers = new HashMap<>();
		headers.put("${auth_header}", "${auth_key}");
		headers.put("Server-Id", "${server_id}");
		
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
			
			FactorySettingsBuilder fsb = FactorySettingsBuilder.create()
				.addParameter("auth_header", "authorize-key")
				.addParameter("auth_key", "my-secret-key--DD-ee-FF")
				.addParameter("auth_key_client", "My-Secret-Key--zz-yy-xx")
				.addParameter("server_id", "1234567890_11")
				.addParameter("client_id", "0987654321_00")
				.setHttpHeaders(headers);
			
			LoadFromHttpUsingHeadersWithVars loadFromHttp = getSingleConfigInstance(fsb, LoadFromHttpUsingHeadersWithVars.class);
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
}


enum Info
{
	priority, designed, birthday;
}

enum Lang
{
	Java, Python, JavaScript, C, Cpp, Csh, PHP;
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.languages")
interface Prop_ValueWithPrefix
{
	@BoundProperty(name = ".Java.priority")
	int javaPriority();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.languages")
interface Prop_ValueWithAndWithoutPrefix
{
	@BoundProperty(name = ".C.priority")
	int cPriority();
	
	@BoundProperty(name = "programming.languages.C.designed")
	String cAuthor();
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties")
interface Prop_SourcePathWithVar
{
	@BoundProperty(name = "programming.languages")
	List<Lang> languages();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties", propertyNamePrefix = "programming.${part}")
interface Prop_PrefixWithVar
{
	@BoundProperty(name = ".Python.priority")
	int pythonPriority();
}

@BoundObject(sourcePath = "./configs/variable_substitution.properties")
interface Prop_ValueWithVar
{
	@BoundProperty(name = "programming.languages.${lang}.designed")
	String langAuthor();
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "programming.${part}")
interface Prop_VarInAllParts
{
	@BoundProperty(name = ".${lang}.priority")
	int langPriority();
	
	@BoundProperty(name = "programming.${part}.${lang}.birthday", customType = DateType.DateOnly.class)
	Date langBirthday();
}


@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "${globalPrefix}")
interface DynamicPropString_VarInAllParts
{
	@BoundProperty(name = ".${part}.@{langName}.@{info}")
	String langInfo(String langName, String info);
	
	@BoundProperty(name = "programming.${part}.@{langName}.designed")
	String langAuthor(String langName);
}

@BoundObject(sourcePath = "./${subfolder}/variable_substitution.properties", propertyNamePrefix = "${globalPrefix}")
interface DynamicPropStringEnum_VarInAllParts
{
	@BoundProperty(name = ".${part}.@{lang}.@{info}")
	String langInfo(Lang lang, Info info);
}

class DynamicPropConfigChangeListener implements IConfigChangeListener
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
		Assertions.assertEquals(DynamicProp_VarInAllParts_TrackChanges.class, configChangedEvent.getConfigInterface());
		Assertions.assertEquals("./configs/variable_substitution.track_changes.properties", configChangedEvent.getSourcePath());
		Map<String, ConfigChangedEvent.ValuesPair> rawValues = configChangedEvent.getRawValues();
		Assertions.assertEquals(4, rawValues.size());
		
		Assertions.assertEquals("1", rawValues.get("programming.languages.Java.priority").getOldValue());
		Assertions.assertEquals("0", rawValues.get("programming.languages.Java.priority").getNewValue());
		Assertions.assertEquals("Guido van Rossum", rawValues.get("programming.languages.Python.designed").getOldValue());
		Assertions.assertEquals("Guido", rawValues.get("programming.languages.Python.designed").getNewValue());
		Assertions.assertEquals("2000-01-01", rawValues.get("programming.languages.Csh.birthday").getOldValue());
		Assertions.assertEquals("2000-01", rawValues.get("programming.languages.Csh.birthday").getNewValue());
		Assertions.assertEquals("functional, logic", rawValues.get("programming.paradigm.declarative").getOldValue());
		Assertions.assertEquals("final result", rawValues.get("programming.paradigm.declarative").getNewValue());
		wasCalled = true;
	}
}

@BoundObject(sourcePath = "./${subfolder}/${configName}", propertyNamePrefix = "${globalPrefix}", trackingPolicy = TrackPolicy.ENABLE, changeListener = DynamicPropConfigChangeListener.class)
interface DynamicProp_VarInAllParts_TrackChanges
{
	@BoundProperty(name = ".${part}.@{lang}.@{info}")
	String langInfo(Lang lang, Info info);
	
	@BoundProperty(name = "programming.paradigm.declarative")
	List<String> declarativeParadigms();
}

@BoundObject(sourcePath = "http://" + SimpleHttpServer.hostName + ":" + SimpleHttpServer.port + DownloadFileHandler.PATH + "http_config.properties", sourceScheme = SourceScheme.HTTP,
	httpHeaders = {"Authorize-Key:${auth_key_client}", "Client-Id:${client_id}"})
interface LoadFromHttpUsingHeadersWithVars
{
	@BoundProperty(name = "config.name")
	String getConfigName();
}
