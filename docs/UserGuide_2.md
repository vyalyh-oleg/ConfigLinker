## User Guide 2

*This is a continuation of **User Guide 1**.*

The following aspects will be considered:
<br/>

**`@BoundObject`**

- sourceScheme (HTTP)
- httpHeaders
- trackingPolicy
- trackingInterval
- changeListener
- errorBehavior

**`@BoundProperty`**

- customType
- deserializationMethod
- errorBehavior


It will also be told about:

- `FactorySettingsBuilder`
- inheritance of properties in `FactorySettings`, `@BoundObject`, `@BoundProperty`
- logging

<br/>


### @BoundObject - sourceScheme (HTTP) / httpHeaders

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
```
<br/>


### @BoundObject - trackingPolicy

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


### @BoundObject - trackingInterval

This parameter is taken into account only for `SourceScheme.HTTP` and only when `TrackPolicy.ENABLE`.  
Otherwise this parameter is ignored.

Default value is '0' which means inherited behaviour (will be used value from `FactorySettingsBuilder` ('60' seconds).

- MIN value = 15 seconds
- MAX value = 86400 seconds (1 day = 24 hours * 3600 seconds).

<br/>


### @BoundObject - changeListener

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


### @BoundObject - errorBehavior

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


### @BoundProperty - customType / deserializationMethod

**`Class<?> customType()`**

Default value is `Object.class`, which mean automatic detection for standard supported return types.
So you no need to change anything if you use them:

- all primitives and arrays of primitives;
- all wrappers of primitives and arrays of them;
- `String` and `String[]`;
- `Enum` and `Enum[]`;
- `List<String>`;
- `Set<String>`;
- `Map<String,String>`;
<br/>

(1) If you want to use **custom return type (or array of custom types)**, you must just implement deserialization logic for your type, and no need any changes in the current parameter.

(2) If you want to use **custom return type as generic type** for `List<CustomType>`, `Set<CustomType>` or `Map<String,CustomType>`, you must just properly specify it's generic type in the angle brackets and implement deserialization logic.

Deserialization logic should be encapsulated in one of the methods, described in `DeserializationMethod` enum. And set choice in `@BoundProperty.deserializationMethod`.
<br>


Only if the deserialization method resides not in your custom type, but in other place (other class), set here it class:  
```java
@BoundObject(sourcePath = "configs/some.properties")
interface TypeDateErrorMilliseconds
{
	@BoundProperty(name = "type.Date.milliseconds", customType = DateType.Milliseconds.class)
	Date getDateTimeFromMilliseconds();
}
```


**`deserializationMethod`**

If you want to use custom return type, you must just implement deserialization logic for it and point the type of `DeserializationMethod` method here.

Default value is `DeserializationMethod.AUTO`, so by default, the appropriate deserialization method will be tried to found out automatically.  
If your class implements multiple deserialization variants, you must choose appropriate value manually.  

If the return type for you configuration method is `List<CustomType>`, `Set<CustomType>` or `Map<String,CustomType>` (thus your custom type is a generic parameter), then only `CONSTRUCTOR_STRING`, `VALUEOF_STRING`, `DESERIALIZER_STRING` allowed as deserialization methods for custom type.
<br/>


`DeserializationMethod`

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


### Predefined deserializers

net.crispcode.configlinker.deserializers.DateType


<br/>

### FactorySettingsBuilder and inheritance of parameters



<br/>

