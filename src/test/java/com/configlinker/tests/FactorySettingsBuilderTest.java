package com.configlinker.tests;

import com.configlinker.FactorySettingsBuilder;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.enums.ErrorBehavior;
import com.configlinker.enums.SourceScheme;
import com.configlinker.enums.TrackPolicy;
import com.configlinker.enums.Whitespaces;
import com.configlinker.exceptions.FactoryConfigBuilderClosedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class FactorySettingsBuilderTest extends AbstractBaseTest
{
	@Test
	void test_defaultParameters()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create();
		
		Assertions.assertEquals(SourceScheme.FILE, factorySettingsBuilder.getSourceScheme());
		Assertions.assertEquals(StandardCharsets.UTF_8, factorySettingsBuilder.getCharset());
		Assertions.assertEquals(Whitespaces.IGNORE, factorySettingsBuilder.getWhitespaces());
		Assertions.assertEquals(ErrorBehavior.THROW_EXCEPTION, factorySettingsBuilder.getErrorBehavior());
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getParameters());
		Assertions.assertEquals(TrackPolicy.DISABLE, factorySettingsBuilder.getTrackPolicy());
		Assertions.assertEquals(60, factorySettingsBuilder.getTrackingInterval());
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getHttpHeaders());
	}
	
	@Test
	void test_inheritParameters()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create();
		
		factorySettingsBuilder.setSourceScheme(SourceScheme.INHERIT);
		factorySettingsBuilder.setWhitespaces(Whitespaces.INHERIT);
		factorySettingsBuilder.setErrorBehavior(ErrorBehavior.INHERIT);
		factorySettingsBuilder.setTrackPolicy(TrackPolicy.INHERIT);
		
		Assertions.assertEquals(SourceScheme.FILE, factorySettingsBuilder.getSourceScheme());
		Assertions.assertEquals(Whitespaces.IGNORE, factorySettingsBuilder.getWhitespaces());
		Assertions.assertEquals(ErrorBehavior.THROW_EXCEPTION, factorySettingsBuilder.getErrorBehavior());
		Assertions.assertEquals(TrackPolicy.DISABLE, factorySettingsBuilder.getTrackPolicy());
	}
	
	@Test
	void test_setAndGetMethods()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create();
		
		factorySettingsBuilder.setSourceScheme(SourceScheme.CLASSPATH);
		factorySettingsBuilder.setCharset(StandardCharsets.UTF_16);
		factorySettingsBuilder.setWhitespaces(Whitespaces.ACCEPT);
		factorySettingsBuilder.setErrorBehavior(ErrorBehavior.RETURN_NULL);
		
		HashMap<String,String> parameters = new HashMap<>();
		parameters.put("param_1", "param_value_1");
		factorySettingsBuilder.setParameters(parameters);
		
		factorySettingsBuilder.setTrackPolicy(TrackPolicy.ENABLE);
		factorySettingsBuilder.setTrackingInterval(15);
		
		HashMap<String,String> headers = new HashMap<>();
		headers.put("Header-1", "header-value-1");
		factorySettingsBuilder.setHttpHeaders(headers);
		
		// ----------------------------------------
		
		Assertions.assertEquals(SourceScheme.CLASSPATH, factorySettingsBuilder.getSourceScheme());
		Assertions.assertEquals(StandardCharsets.UTF_16, factorySettingsBuilder.getCharset());
		Assertions.assertEquals(Whitespaces.ACCEPT, factorySettingsBuilder.getWhitespaces());
		Assertions.assertEquals(ErrorBehavior.RETURN_NULL, factorySettingsBuilder.getErrorBehavior());
		Assertions.assertEquals(parameters, factorySettingsBuilder.getParameters());
		Assertions.assertEquals(TrackPolicy.ENABLE, factorySettingsBuilder.getTrackPolicy());
		Assertions.assertEquals(15, factorySettingsBuilder.getTrackingInterval());
		Assertions.assertEquals(headers, factorySettingsBuilder.getHttpHeaders());
		
		// ----------------------------------------
		
		parameters.put("param_2", "param_value_2");
		factorySettingsBuilder.addParameter("param_2", "param_value_2");
		Assertions.assertEquals(parameters, factorySettingsBuilder.getParameters());
		
		headers.put("Header-2", "header-value-2");
		factorySettingsBuilder.addHttpHeader("Header-2", "header-value-2");
		Assertions.assertEquals(headers, factorySettingsBuilder.getHttpHeaders());
		
		// ----------------------------------------
		
		factorySettingsBuilder.setParameters(new HashMap<>());
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getParameters());
		
		factorySettingsBuilder.setHttpHeaders(new HashMap<>());
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getHttpHeaders());
		
		factorySettingsBuilder.addParameter("param_2", "param_value_2");
		factorySettingsBuilder.setParameters(null);
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getParameters());
		
		
		factorySettingsBuilder.addHttpHeader("Header-2", "header-value-2");
		factorySettingsBuilder.setHttpHeaders(null);
		Assertions.assertEquals(Collections.emptyMap(), factorySettingsBuilder.getHttpHeaders());
		
		// ----------------------------------------
		
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
		  () -> factorySettingsBuilder.setTrackingInterval(14));
		Assertions.assertEquals("Tracking interval can not be less than 15 seconds and greater than 86400 seconds (24 hours). You set '14' seconds.", exception.getMessage());
		
		exception = Assertions.assertThrows(IllegalArgumentException.class,
		  () -> factorySettingsBuilder.setTrackingInterval(86401));
		Assertions.assertEquals("Tracking interval can not be less than 15 seconds and greater than 86400 seconds (24 hours). You set '86401' seconds.", exception.getMessage());
	}
	
	@Test
	void test_builderCloseAfterFactoryCreation()
	{
		FactorySettingsBuilder factorySettingsBuilder = FactorySettingsBuilder.create();
		NonEmptyValue nonEmptyValue = getSingleConfigInstance(factorySettingsBuilder, NonEmptyValue.class);
		
		FactoryConfigBuilderClosedException exception = Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setSourceScheme(SourceScheme.CLASSPATH));
		
		Assertions.assertEquals(
		  "You can not change parameter or add property to closed FactorySettingsBuilder. It is closed, because it was already used in ConfigSetFactory.",
		  exception.getMessage());
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setCharset(StandardCharsets.UTF_16));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setWhitespaces(Whitespaces.ACCEPT));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setErrorBehavior(ErrorBehavior.RETURN_NULL));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setParameters(null));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setTrackPolicy(TrackPolicy.ENABLE));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setTrackingInterval(15));
		
		Assertions.assertThrows(FactoryConfigBuilderClosedException.class,
		  () -> factorySettingsBuilder.setHttpHeaders(null));
	}
}


@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface NonEmptyValue
{
	@BoundProperty(name = "workgroup.non_empty")
	String non_empty();
}

@BoundObject(sourcePath = "configs/bound_property_functionality.properties")
interface AnotherNonEmptyValue
{
	@BoundProperty(name = "workgroup.non_empty")
	String non_empty();
}
