package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.deserializers.DateType;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ExtendedTypesTest extends AbstractBaseTest
{
	@Test
	void test_typeEnum()
	{
		TypeEnum typeEnum = getSingleConfigInstance(TypeEnum.class);
		
		Assertions.assertEquals(NumberName.three, typeEnum.getNumber());
		Assertions.assertEquals(NumberName.three.name(), typeEnum.getNumberAsString());
	}
	
	@Test
	void test_typeEnumError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeEnumError typeEnumError = getSingleConfigInstance(TypeEnumError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'com.configlinker.tests.NumberName::valueOf'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(IllegalArgumentException.class, baseCause.getClass());
		Assertions.assertEquals("No enum constant com.configlinker.tests.NumberName.eleven", baseCause.getMessage());
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
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.net.URL::java.net.URL'.", exception.getMessage());
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
			TypeURIError typeURIError = getSingleConfigInstance(TypeURIError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.net.URI::java.net.URI'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(URISyntaxException.class, baseCause.getClass());
		Assertions.assertEquals("Expected scheme name at index 0: :mailto:John.Doe@example.com", baseCause.getMessage());
	}
	
	@Test
	void test_typeUUID()
	{
		TypeUUID typeURIError = getSingleConfigInstance(TypeUUID.class);
		
		Assertions.assertEquals("1711e708-a486-4dc5-8dbe-f275b601064f", typeURIError.getUUIDAsString());
		Assertions.assertEquals("1711e708-a486-4dc5-8dbe-f275b601064f", typeURIError.getUUID().toString());
	}
	
	@Test
	void test_typeUUIDError1()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeUUIDError1 typeURIError = getSingleConfigInstance(TypeUUIDError1.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.util.UUID::fromString'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"4du5\"", baseCause.getMessage());
	}
	
	@Test
	void test_typeUUIDError2()
	{
		TypeUUIDError2 typeURIError = getSingleConfigInstance(TypeUUIDError2.class);
		
		Assertions.assertEquals("1711e78e-a4864dc5-8dbe-f275b601-064f", typeURIError.getUUIDAsString());
		Assertions.assertEquals("1711e78e-4dc5-8dbe-b601-00000000064f", typeURIError.getUUID().toString());
	}
	
	@Test
	void test_typeInetAddress()
	{
		TypeInetAddress typeInetAddress = getSingleConfigInstance(TypeInetAddress.class);
		
		Assertions.assertEquals("192.168.12.10", typeInetAddress.getInetAddressIPv4AsString());
		Assertions.assertEquals("192.168.12.10", typeInetAddress.getInetAddressIPv4().getHostAddress());
		Assertions.assertEquals("192.168.12.10", typeInetAddress.getInetAddressIPv4_().getHostAddress());
		
		Assertions.assertEquals("::123", typeInetAddress.getInetAddressIPv6AsString_1());
		Assertions.assertEquals("0:0:0:0:0:0:0:123", typeInetAddress.getInetAddressIPv6_1().getHostAddress());
		Assertions.assertEquals("0:0:0:0:0:0:0:123", typeInetAddress.getInetAddressIPv6_11().getHostAddress());
		
		Assertions.assertEquals("::ffff", typeInetAddress.getInetAddressIPv6AsString_2());
		Assertions.assertEquals("0:0:0:0:0:0:0:ffff", typeInetAddress.getInetAddressIPv6_2().getHostAddress());
		Assertions.assertEquals("0:0:0:0:0:0:0:ffff", typeInetAddress.getInetAddressIPv6_22().getHostAddress());
		
		Assertions.assertEquals("fe80::fc02:bcff:fea3:919b", typeInetAddress.getInetAddressIPv6AsString_3());
		Assertions.assertEquals("fe80:0:0:0:fc02:bcff:fea3:919b", typeInetAddress.getInetAddressIPv6_3().getHostAddress());
		Assertions.assertEquals("fe80:0:0:0:fc02:bcff:fea3:919b", typeInetAddress.getInetAddressIPv6_33().getHostAddress());
	}
	
	@Test
	void test_typeInetAddressIPv4Error()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeInetAddressIPv4Error typeInetAddressIPv4Error = getSingleConfigInstance(TypeInetAddressIPv4Error.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.net.InetAddress::getByName'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(UnknownHostException.class, baseCause.getClass());
		Assertions.assertEquals("192.256.12.10: Name or service not known", baseCause.getMessage());
	}
	
	@Test
	void test_testInetAddressIPv6Error()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeInetAddressIPv6Error typeInetAddressIPv6Error = getSingleConfigInstance(TypeInetAddressIPv6Error.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'java.net.InetAddress::getByName'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(UnknownHostException.class, baseCause.getClass());
		Assertions.assertEquals(":::123: invalid IPv6 address", baseCause.getMessage());
	}
	
	@Test
	void test_typeDate(TestReporter testReporter)
	{
		TypeDate typeDate = getSingleConfigInstance(TypeDate.class);
		
		Assertions.assertEquals(new Date(12345678909876L), typeDate.getDateTimeFromMilliseconds());
		Assertions.assertEquals(new Date(1530375271000L), typeDate.getDateTimeFromSeconds());
		Assertions.assertEquals(new Date(915148800000L), typeDate.getYearOnly());
		Assertions.assertEquals(new Date(1436832000000L), typeDate.getDateOnly());
		Assertions.assertEquals(new Date(68207000L), typeDate.getTimeOnly());
		Assertions.assertEquals(new Date(1406919697000L), typeDate.getDateTime());
		Assertions.assertEquals(new Date(1406910697000L), typeDate.getDateTimeWithZone());
		Assertions.assertEquals(new Date(1008808797523L), typeDate.getDateTime_RFC_3339());
		Assertions.assertEquals(new Date(784111777000L), typeDate.getDateTime_RFC_822_1123());
		Assertions.assertEquals(new Date(784111777000L), typeDate.getDateTime_RFC_850_1036());
	}
	
	@Test
	void test_typeDateErrorMilliseconds()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeDateErrorMilliseconds typeInetAddressIPv6Error = getSingleConfigInstance(TypeDateErrorMilliseconds.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'com.configlinker.deserializers.DateType$Milliseconds::deserialize'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"123456O78909876\"", baseCause.getMessage());
		
	}
	
	@Test
	void test_typeDateErrorSeconds()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeDateErrorSeconds typeInetAddressIPv6Error = getSingleConfigInstance(TypeDateErrorSeconds.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'com.configlinker.deserializers.DateType$Seconds::deserialize'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause();
		Assertions.assertEquals(NumberFormatException.class, baseCause.getClass());
		Assertions.assertEquals("For input string: \"15303752711234567890\"", baseCause.getMessage());
	}
	
	@Test
	void test_typeDateError()
	{
		PropertyMapException exception = Assertions.assertThrows(PropertyMapException.class, () -> {
			TypeDateError typeInetAddressIPv6Error = getSingleConfigInstance(TypeDateError.class);
		});
		
		Assertions.assertEquals("Cannot create object for return type in method 'com.configlinker.deserializers.DateType$DateTime::deserialize'.", exception.getMessage());
		Throwable baseCause = exception.getCause().getCause().getCause();
		Assertions.assertEquals(ParseException.class, baseCause.getClass());
		Assertions.assertEquals("Unparseable date: \"2014-08-01 19:01:37\"", baseCause.getMessage());
	}
}


enum NumberName
{
	one, two, three, four, five
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeEnum
{
	@BoundProperty(name = "type.Enum.numberName")
	NumberName getNumber();
	
	@BoundProperty(name = "type.Enum.numberName")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeEnumError
{
	@BoundProperty(name = "type.Enum.numberName.wrong")
	NumberName getNumber();
	
	@BoundProperty(name = "type.Enum.numberName.wrong")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeURL
{
	@BoundProperty(name = "type.URL")
	URL getURL();
	
	@BoundProperty(name = "type.URL")
	String getURLAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeURLError
{
	@BoundProperty(name = "type.URL.wrong")
	URL getURL();
	
	@BoundProperty(name = "type.URL.wrong")
	String getURLAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
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

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeURIError
{
	@BoundProperty(name = "type.URI.wrong")
	URI getNumber();
	
	@BoundProperty(name = "type.URI.wrong")
	String getNumberAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeUUID
{
	@BoundProperty(name = "type.UUID")
	UUID getUUID();
	
	@BoundProperty(name = "type.UUID")
	String getUUIDAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeUUIDError1
{
	@BoundProperty(name = "type.UUID.wrong.1")
	UUID getUUID();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeUUIDError2
{
	@BoundProperty(name = "type.UUID.wrong.2")
	UUID getUUID();
	
	@BoundProperty(name = "type.UUID.wrong.2")
	String getUUIDAsString();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeInetAddress
{
	@BoundProperty(name = "type.InetAddress.IPv4")
	String getInetAddressIPv4AsString();
	
	@BoundProperty(name = "type.InetAddress.IPv4")
	InetAddress getInetAddressIPv4();
	
	@BoundProperty(name = "type.InetAddress.IPv4")
	Inet4Address getInetAddressIPv4_();
	
	@BoundProperty(name = "type.InetAddress.IPv6.1")
	String getInetAddressIPv6AsString_1();
	
	@BoundProperty(name = "type.InetAddress.IPv6.1")
	InetAddress getInetAddressIPv6_1();
	
	@BoundProperty(name = "type.InetAddress.IPv6.1")
	Inet6Address getInetAddressIPv6_11();
	
	@BoundProperty(name = "type.InetAddress.IPv6.2")
	String getInetAddressIPv6AsString_2();
	
	@BoundProperty(name = "type.InetAddress.IPv6.2")
	InetAddress getInetAddressIPv6_2();
	
	@BoundProperty(name = "type.InetAddress.IPv6.2")
	Inet6Address getInetAddressIPv6_22();
	
	@BoundProperty(name = "type.InetAddress.IPv6.3")
	String getInetAddressIPv6AsString_3();
	
	@BoundProperty(name = "type.InetAddress.IPv6.3")
	InetAddress getInetAddressIPv6_3();
	
	@BoundProperty(name = "type.InetAddress.IPv6.3")
	Inet6Address getInetAddressIPv6_33();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeInetAddressIPv4Error
{
	@BoundProperty(name = "type.InetAddress.IPv4.wrong")
	InetAddress getInetAddress();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeInetAddressIPv6Error
{
	@BoundProperty(name = "type.InetAddress.IPv6.wrong")
	InetAddress getInetAddress();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeDate
{
	@BoundProperty(name = "type.Date.milliseconds", customTypeOrDeserializer = DateType.Milliseconds.class)
	Date getDateTimeFromMilliseconds();
	
	@BoundProperty(name = "type.Date.seconds", customTypeOrDeserializer = DateType.Seconds.class)
	Date getDateTimeFromSeconds();
	
	@BoundProperty(name = "type.Date.year", customTypeOrDeserializer = DateType.Year.class)
	Date getYearOnly();
	
	@BoundProperty(name = "type.Date.date", customTypeOrDeserializer = DateType.DateOnly.class)
	Date getDateOnly();
	
	@BoundProperty(name = "type.Date.time", customTypeOrDeserializer = DateType.TimeOnly.class)
	Date getTimeOnly();
	
	@BoundProperty(name = "type.Date.date-time", customTypeOrDeserializer = DateType.DateTime.class)
	Date getDateTime();
	
	@BoundProperty(name = "type.Date.date-time-zone", customTypeOrDeserializer = DateType.DateTimeZone.class)
	Date getDateTimeWithZone();
	
	@BoundProperty(name = "type.Date.RFC_3339", customTypeOrDeserializer = DateType.TimestampRFC_3339.class)
	Date getDateTime_RFC_3339();
	
	@BoundProperty(name = "type.Date.RFC_822_1123", customTypeOrDeserializer = DateType.TimestampRFC_822_1123.class)
	Date getDateTime_RFC_822_1123();
	
	@BoundProperty(name = "type.Date.RFC_850_1036", customTypeOrDeserializer = DateType.TimestampRFC_850_1036.class)
	Date getDateTime_RFC_850_1036();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeDateErrorMilliseconds
{
	@BoundProperty(name = "type.Date.milliseconds.wrong", customTypeOrDeserializer = DateType.Milliseconds.class)
	Date getDateTimeFromMilliseconds();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeDateErrorSeconds
{
	@BoundProperty(name = "type.Date.seconds.wrong", customTypeOrDeserializer = DateType.Seconds.class)
	Date getDateTimeFromSeconds();
}

@BoundObject(sourcePath = "configs/extended_types.properties")
interface TypeDateError
{
	@BoundProperty(name = "type.Date.date-time.wrong", customTypeOrDeserializer = DateType.DateTime.class)
	Date getDateTime();
}
