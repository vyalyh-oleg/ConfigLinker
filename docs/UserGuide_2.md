# User Guide 2

Other articles:

- [ReadMe](../README.md)
- [Quick Start Guide (5 minutes)](QuickStartGuide_5m.md)
- [User Guide part 1](UserGuide_1.md)
<br/>

*This is a continuation of **User Guide 1**.*

The following aspects will be considered:
<br/>

**`@BoundObject`**

- [sourceScheme (HTTP)](#boundobjectsourcescheme-http-boundobjecthttpheaders)
- [httpHeaders](#boundobjectsourcescheme-http-boundobjecthttpheaders)
- [trackingPolicy](#boundobjecttrackingpolicy)
- [trackingInterval](#boundobjecttrackinginterval)
- [changeListener](#boundobjectchangelistener)
- [errorBehavior](#boundobjecterrorbehavior)

**`@BoundProperty`**

- [customType](#boundpropertycustomtype-boundpropertydeserializationmethod)
- [deserializationMethod](#boundpropertycustomtype-boundpropertydeserializationmethod)
- [errorBehavior](#boundobjecterrorbehavior)


It will also be told about:

- [Predefined deserializers](#predefined-deserializers)
- [`FactorySettingsBuilder` and inheritance of properties in `FactorySettings`, `@BoundObject`, `@BoundProperty`](#factorysettingsbuilder-and-inheritance-of-parameters)
- [logging in the library](#logging-in-the-library)

<br/>


### @BoundObject.sourceScheme (HTTP), @BoundObject.httpHeaders

Describes the type of the source that is used to retrieve property values for this annotated object.  
Other source types are described in *User Guide 1*, and here we'll discuss **`SourceScheme.HTTP`**.

So, when HTTP `sourceScheme` is used, the valid URL in **`BoundObject.sourcePath`** should be set. Of course it also supports `${substitution}` (see *User Guide 1 - @BoundProperty - name*).

The library will make http/s request(s) to retrieve the data. The raw data should be in the same well known `properties` format.
<br/>

It is also possible to set custom http headers in **`@BoundObject.httpHeaders`**, which will be sent with the request to the server.

They are just an array of `String[]`.  
These values are merged with the values set with `FactorySettingsBuilder.addHttpHeader|setHttpHeaders`.  
`${variables}` can be used for substituting some parts of the header.
<br/>

<u>Example:</u>
```java
@BoundObject(sourcePath = "http://${affiliate}.mycompany.com/config/auth_props",
             httpHeaders = { "Auth-Token: ${auth_token}",
                             "Other-Header: value",
                             "Cookie: favorite=1; permission=admin"}
            )
interface Auth
{ 
  // methods for retrieving configuration values
}
```
<br/>


### @BoundObject.trackingPolicy

Policy for refresh configuration parameters.

Default value is `TrackPolicy.INHERIT` which mean global policy will be used (from the `FactorySettingsBuilder.setTrackPolicy()`).

Default global policy is `TrackPolicy.DISABLE`.  
To override such behaviour choose `TrackPolicy.DISABLE` or `TrackPolicy.ENABLE` value either for specific `@BoundObject` interface or for `FactorySettingsBuilder`.
<br/>

**`DISABLE`** state  
All changes, made in the underlying persistent configuration store, won't be tracked. The values will be loaded and cached only once during program startup.
<br/>

**`ENABLE`** state  
All changes, made in the underlying persistent configuration store, will be tracked.

The behavior depends on `SourceScheme`:

- `SourceScheme.CLASSPATH` -- track changes not allowed for this scheme.
- `SourceScheme.FILE` -- listen OS notification from the file system. Should be supported by operation system.
- `SourceScheme.HTTP` -- refresh values from the remote store by schedule with specified period of time.
- `SourceScheme.CONFIG_LINKER_SERVER` -- listen notification from remote ConfigLinker server (not implemented yet).

<br/>


### @BoundObject.trackingInterval

This parameter is taken into account only for `SourceScheme.HTTP` and only when `TrackPolicy.ENABLE`.  
Otherwise this parameter is ignored.

Default value is '0' which means inherited behaviour (will be used value from `FactorySettingsBuilder` ('60' seconds).

- MIN value = 15 seconds
- MAX value = 86400 seconds (1 day = 24 hours * 3600 seconds).

<br/>


### @BoundObject.changeListener

It is intended for receiving notifications on configuration updates (so TrackPolicy should be ENABLE).

Points to the class which implements the interface `IConfigChangeListener`.  
Events are handled in the method: `configChanged(ConfigChangedEvent configChangedEvent)`.
<br/>

`ConfigChangedEvent` contains information about changes were made in the configuration sources.  
Available event infromation:

- `getConfigInterface()` -- returns the `Class<?>` of annotated @BoundObject interface;
- `getSourcePath()` -- returns `String` with the path to the source;
- `getRawValues()` -- returns `Map<String, ValuesPair>` keys and values (old, new) that were changed. Can return null (if the error happened before new properties have been loaded).
- `getException()` -- returns the exception with the reason if an error occurred during configuration update. If this method returns not null value, method getRawValues() returns null.

<br/>


### @BoundObject.errorBehavior

It says what to do if the property value does not exist in underlying persistent store or cannot be converted to object representation for any reasons.

Default value is `ErrorBehavior.INHERIT` from the `FactorySettingsBuilder.setErrorBehavior(ErrorBehavior)`.
<br/>

This is useful only in two cases:

- if ConfigLinker library found changes in one of the tracked property file during runtime and doesn't able to deal with them;
- if the property has runtime dynamic parts (in other words this is mean that methods in your interfaces have parameters) and somewhere in other code the non-existent part was passed to this method.
<br/>

`ErrorBehavior.THROW_EXCEPTION` -- throws PropertyNotFoundException when the value for configuration property wasn't found. Or other exceptions, deriviated from ConfigLinkerRuntimeException. It is default behaviour when INHERIT.

`ErrorBehavior.RETURN_NULL` -- returns 'null' as property value on any error.
<br/>

**`@BoundProperty.errorBehavior`**  
has the same meaning but for specific configuration value (not for entire interface).

<br/>


### @BoundProperty.customType, @BoundProperty.deserializationMethod

**`Class<?> customType()`**

Default value is `Object.class`, which mean automatic detection for [standard automatically supported return types](UserGuide_1.md#automatically-supported-return-types).
<br/>

(1) If you want to use **custom return type (or array of custom types)**, you must just implement deserialization logic for your type, and no need any changes in the current parameter.

(2) If you want to use **custom return type as generic type** for `List<CustomType>`, `Set<CustomType>` or `Map<String,CustomType>`, you must just properly specify it's generic type in the angle brackets and implement deserialization logic in your `CustomType`.

**Deserialization logic** should be implemented in one of the methods, described in `DeserializationMethod` enum. The approach should be set in `@BoundProperty.deserializationMethod`.


**Note:** *Only if the deserialization method resides not in your custom type, but in other place (other class implements interface `IDeserializer<>`), set here it class type.*  
```java
class CompanyDeserializer implements IDeserializer<Company>
{
	@Override
   	public Company deserialize(Map<String, String> stringValues)
   	{
   		// deserialization logic
   	}
}

@BoundObject(sourcePath = "configs/some.properties")
interface TypeDateErrorMilliseconds
{
	@BoundProperty(name = "type.Date.milliseconds", customType = CompanyDeserializer.class)
	Company getCompany();
}
```
<br/>

**`deserializationMethod`**

If you want to use custom return type, you must just implement deserialization logic for it and point the type of `DeserializationMethod` here.

Default value is `DeserializationMethod.AUTO`, so by default, the appropriate deserialization method will be tried to found out automatically.  
If your class implements multiple deserialization variants, you must choose appropriate value manually.  

If the return type for you configuration method is `List<CustomType>`, `Set<CustomType>` or `Map<String,CustomType>` (thus your custom type is a generic parameter), then only `CONSTRUCTOR_STRING`, `VALUEOF_STRING`, `DESERIALIZER_STRING` allowed as deserialization methods for custom type.
<br/>


**`DeserializationMethod`** enum

- `CONSTRUCTOR_STRING`  
  implement constructor `'public/private CustomReturnType(String raw)`;
- `CONSTRUCTOR_MAP`  
  implement constructor `public/private CustomReturnType(Map<String,String> raw)`;
- `VALUEOF_STRING`  
  implement in your custom type static instance generator method `public/private static CustomReturnType valueOf(String raw)`;
- `VALUEOF_MAP`  
  implement in your custom type static instance generator method `public/private static CustomReturnType valueOf(Map<String,String> raw)`
- `DESERIALIZER_STRING`  
  chose this variant if your class implements interface `IDeserializer` and method `deserialize(String rawValue)`
- `DESERIALIZER_MAP`  
  chose this variant if your class implements interface `IDeserializer` and method `deserialize(Map stringValues)`

<br/>


<u>Example:</u>

The below class implements various deserialization methods. They are gathered in one class just for illustration.

```java
class Company implements IDeserializer<Company>
{
	String name;
	String[] emails;
	String[] phoneNumbers;
	String ceo;
	short dateFoundation;
	double authorizedCapital;
	DeserializationMethod deserializationMethod;
	
	public Company()
	{
	}
	
    // DeserializationMethod.VALUEOF_STRING
	public static Company valueOf(String rawStringData)
	{
		Company newCompany = new Company();
		parseFromString(newCompany, rawStringData);
		newCompany.deserializationMethod = DeserializationMethod.VALUEOF_STRING;
		return newCompany;
	}
	
	// DeserializationMethod.VALUEOF_MAP
	public static Company valueOf(Map<String, String> rawStringMap)
	{
		Company newCompany = new Company();
		parseFromMap(newCompany, rawStringMap);
		newCompany.deserializationMethod = DeserializationMethod.VALUEOF_MAP;
		return newCompany;
	}
	
	private static void parseFromString(Company company, String rawStringData)
	{
		String[] parts = rawStringData.split("//");
		company.name = parts[0].trim();
		company.emails = Arrays.stream(parts[1].split(";")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(parts[2].split(";")).map(String::trim).toArray(String[]::new);
		company.ceo = parts[3].trim();
		company.dateFoundation = Short.parseShort(parts[4].trim());
		company.authorizedCapital = Double.parseDouble(parts[5].trim());
	}
	
	private static void parseFromMap(Company company, Map<String, String> rawKeyValues)
	{
		company.name = rawKeyValues.get("name");
		company.emails = Arrays.stream(rawKeyValues.get("emails").split(";")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(rawKeyValues.get("phoneNumbers").split(";")).map(String::trim).toArray(String[]::new);
		company.ceo = rawKeyValues.get("ceo");
		company.dateFoundation = Short.parseShort(rawKeyValues.get("dateFoundation"));
		company.authorizedCapital = Double.parseDouble(rawKeyValues.get("authorizedCapital"));
	}
	
    // DeserializationMethod.CONSTRUCTOR_STRING
	public Company(String rawData)
	{
		parseFromString(this, rawData);
		this.deserializationMethod = DeserializationMethod.CONSTRUCTOR_STRING;
	}
	
    // DeserializationMethod.CONSTRUCTOR_MAP
	public Company(Map<String, String> rawStringMap)
	{
		parseFromMap(this, rawStringMap);
		this.deserializationMethod = DeserializationMethod.CONSTRUCTOR_MAP;
	}
	
    // DeserializationMethod.DESERIALIZER_STRING
	@Override
	public Company deserialize(String rawValue)
	{
		Company newCompany = new Company();
		parseFromString(newCompany, rawValue);
		newCompany.deserializationMethod = DeserializationMethod.DESERIALIZER_STRING;
		return newCompany;
	}
	
    // DeserializationMethod.DESERIALIZER_STRING
	@Override
	public Company deserialize(Map<String, String> stringValues)
	{
		Company newCompany = new Company();
		parseFromMap(newCompany, stringValues);
		newCompany.deserializationMethod = DeserializationMethod.DESERIALIZER_MAP;
		return newCompany;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Company company = (Company) o;
		return dateFoundation == company.dateFoundation &&
		  Double.compare(company.authorizedCapital, authorizedCapital) == 0 &&
		  Objects.equals(name, company.name) &&
		  Arrays.equals(emails, company.emails) &&
		  Arrays.equals(phoneNumbers, company.phoneNumbers) &&
		  Objects.equals(ceo, company.ceo);
	}
	
	@Override
	public int hashCode()
	{
		int result = Objects.hash(name, ceo, dateFoundation, authorizedCapital);
		result = 31 * result + Arrays.hashCode(emails);
		result = 31 * result + Arrays.hashCode(phoneNumbers);
		return result;
	}
	
	@Override
	public String toString()
	{
		return "Company{" +
		  "name='" + name + '\'' +
		  ", emailsList=" + Arrays.toString(emails) +
		  ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
		  ", ceo='" + ceo + '\'' +
		  ", dateFoundation=" + dateFoundation +
		  ", authorizedСapital=" + authorizedCapital +
		  '}';
	}
}
```
<br/>


### Predefined deserializers

The library contains default deserializers for the `Date` type:

`net.crispcode.configlinker.deserializers.DateType` enum.

- DateType.Milliseconds.class  
  Milliseconds from epoch.
  
- DateType.Seconds.class  
  Milliseconds from epoch
  
- DateType.Year.class  
  A year in literally form.  
  Example: `2001`, or `18`, or `184`, or `1542`.
  
- DateType.DateOnly.class  
  Date as "yyyy-MM-dd".  
  Example: `'2001-07-04'`.
  
- DateType.TimeOnly.class  
  Time as "HH:mm:ss".
  Example: `'08:56:32'`.
  
- DateType.DateTime.class  
  Date and time as "yyyy-MM-dd'T'HH:mm:ss".  
  Example: `'2001-07-04T12:08:56'`.
  
- DateType.DateTimeZone.class  
  Date, time and zone as "yyyy-MM-dd'T'HH:mm:ssZ".  
  Example: `'2001-07-04T12:08:56-0700'` (here zone is -7 hours).
  
- DateType.TimestampRFC_3339.class  
  Timestamp as "yyyy-MM-dd'T'HH:mm:ss.SSSXXX".  
  Example: `'1996-12-19T16:39:57.523-08:00'`.

- DateType.TimestampRFC_822_1123.class  
  Timestamp as "EEE, dd MMM yyyy HH:mm:ss zz".  
  Example: `'Sun, 06 Nov 1994 08:49:37 GMT'`.

- DateType.TimestampRFC_850_1036.class  
  Timestamp as "EEEEE, dd-MMM-yy HH:mm:ss zz".  
  Example: `'Sunday, 06-Nov-94 08:49:37 GMT'`.


```java
@BoundObject(sourcePath = "configs/some.properties")
interface TypeDateErrorMilliseconds
{
	@BoundProperty(name = "type.Date.milliseconds", customType = DateType.Milliseconds.class)
	Date getDateTimeFromMilliseconds();
}
```
<br/>


### FactorySettingsBuilder and inheritance of parameters
Some parameters used in `@BoundObject` and `@BoundProperty` could be set globally by the `FactorySettingsBuilder`.

In such a case desired parameters in `@BoundObject` should be set in its default state unless you want to override them with your own value.

By default all these values are taken globally:

`@BoundObject`  
- sourceScheme() default SourceScheme.INHERIT
- charsetName() default ""
- trackingPolicy() default TrackPolicy.INHERIT;
- trackingInterval() default 0;
- errorBehavior() default ErrorBehavior.INHERIT;

`@BoundProperty`  
- whitespaces() default Whitespaces.INHERIT;
- errorBehavior() default ErrorBehavior.INHERIT;


In `FactorySettingsBuilder` you can use below methods:
- setSourceScheme
- setCharset
- setTrackPolicy
- setTrackingInterval
- setErrorBehavior
- setWhitespaces


The headers (for sourceScheme==HTTP) are merged if you set them both in `@BoundObject.httpHeaders` and `FactorySettingsBuilder.setHttpHeaders`.
<br/>


### Logging in the library

The library use `slf4j` api.

All library logging messages fall into this logger: `'net.crispcode.ConfigLinker'`.

Thus you can override the behaviour/level/appender for it in your logger.xml file.

<br/>
