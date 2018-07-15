package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ExtendedTypesTest extends AbstractBaseTest
{
	@Test
	void test_typeEnum()
	{
		TypeEnum typeEnum = getSingleConfigInstance(TypeEnum.class);
		
		Assertions.assertEquals(NumberNames.three, typeEnum.getNumber());
		Assertions.assertEquals(NumberNames.three.name(), typeEnum.getNumberAsString());
	}
	
	@Test
	void test_typeEnumError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeEnumError typeEnumError = getSingleConfigInstance(TypeEnumError.class);
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'com.configlinker.tests.NumberNames::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(IllegalArgumentException.class, baseCause.getClass());
		Assertions.assertEquals("No enum constant com.configlinker.tests.NumberNames.eleven", baseCause.getMessage());
	}
	
	@Test
	void test_typeURL()
	{
		String urlStr = "http://google.com.ua:8080/path/to;matrix_1=foo;matrix_2=bar/resource.jsf?one=1&two=2#firstpart";
		TypeURL typeURL = getSingleConfigInstance(TypeURL.class);
		
		Assertions.assertEquals(urlStr, typeURL.getURLAsString());
		Assertions.assertEquals(urlStr, typeURL.getURL().toString());
	}
	
	@Test
	void test_typeURLError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeURLError typeURL = getSingleConfigInstance(TypeURLError.class);
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.net.URL::java.net.URL'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(MalformedURLException.class, baseCause.getClass());
		Assertions.assertEquals("unknown protocol: http-", baseCause.getMessage());
	}
	
	@Test
	void test_typeURI()
	{
		String uriStr_1 = "file:///home/john/Documents/charts.pdf";
		String uriStr_2 = "mailto:John.Doe@example.com";
		String uriStr_3 = "ldap://[2001:db8::7]/c=GB?objectClass?one";
		String uriStr_4 = "urn:oasis:names:specification:docbook:dtd:xml:4.1.2";
		TypeURI typeURI = getSingleConfigInstance(TypeURI.class);
		
		Assertions.assertEquals(uriStr_1, typeURI.getURIAsString_1());
		Assertions.assertEquals(uriStr_2, typeURI.getURIAsString_2());
		Assertions.assertEquals(uriStr_3, typeURI.getURIAsString_3());
		Assertions.assertEquals(uriStr_4, typeURI.getURIAsString_4());
		
		Assertions.assertEquals(uriStr_1, typeURI.getURI_1().toString());
		Assertions.assertEquals(uriStr_2, typeURI.getURI_2().toString());
		Assertions.assertEquals(uriStr_3, typeURI.getURI_3().toString());
		Assertions.assertEquals(uriStr_4, typeURI.getURI_4().toString());
	}
	
	@Test
	void test_typeURIError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeURIError typeURIError  = getSingleConfigInstance(TypeURIError.class);
		});
		
		Assertions.assertEquals("Cannot interpret return type for method 'java.net.URI::java.net.URI'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(URISyntaxException.class, baseCause.getClass());
		Assertions.assertEquals("Expected scheme name at index 0: :mailto:John.Doe@example.com", baseCause.getMessage());
	}
}


enum NumberNames
{
	one, two, three, four, five
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeEnum
{
	@BoundProperty(name = "type.Enum.numberName")
	NumberNames getNumber();
	
	@BoundProperty(name = "type.Enum.numberName")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeEnumError
{
	@BoundProperty(name = "type.Enum.numberName.wrong")
	NumberNames getNumber();
	
	@BoundProperty(name = "type.Enum.numberName.wrong")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeURL
{
	@BoundProperty(name = "type.URL")
	URL getURL();
	
	@BoundProperty(name = "type.URL")
	String getURLAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeURLError
{
	@BoundProperty(name = "type.URL.wrong")
	URL getURL();
	
	@BoundProperty(name = "type.URL.wrong")
	String getURLAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeURI
{
	@BoundProperty(name = "type.URI.1")
	URI getURI_1();
	
	@BoundProperty(name = "type.URI.2")
	URI getURI_2();
	
	@BoundProperty(name = "type.URI.3")
	URI getURI_3();
	
	@BoundProperty(name = "type.URI.4")
	URI getURI_4();
	
	@BoundProperty(name = "type.URI.1")
	String getURIAsString_1();
	
	@BoundProperty(name = "type.URI.2")
	String getURIAsString_2();
	
	@BoundProperty(name = "type.URI.3")
	String getURIAsString_3();
	
	@BoundProperty(name = "type.URI.4")
	String getURIAsString_4();
}

@BoundObject(sourcePath = "configs/extended_types.properties" )
interface TypeURIError
{
	@BoundProperty(name = "type.URI.wrong")
	URI getNumber();
	
	@BoundProperty(name = "type.URI.wrong")
	String getNumberAsString();
}

