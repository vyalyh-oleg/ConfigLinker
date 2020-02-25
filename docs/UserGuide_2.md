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

- FactorySettingsBuilder
- inheritance of properties in `FactorySettings`, `@BoundObject`, `@BoundProperty`

<br/>


### @BoundObject - sourceScheme (HTTP) / httpHeaders

Describes the type of the source that is used to retrieve property values for this annotated object.  
Other types are described in *User Guide 1*, and here we'll discuss **`SourceScheme.HTTP`**.  

So, when HTTP `sourceScheme` is used, the valid URL in **`BoundObject.sourcePath`** should be set. Of course it also supports `${substitution}` (see *User Guide 1 - @BoundProperty - name*).  

The library will make the request(s) to retrieve the data. The raw data should be in the same well known `properties` format.
<br/>

It is also possible to set custom http headers in **`@BoundObject.httpHeaders`**, which will be sent with the request to the server.  

They are just an array of `String[]`.  
These values are merged with the values which you can set with `FactorySettingsBuilder.addHttpHeader|setHttpHeaders`.  
`${variables}` can be used for substituting some parts of the header.
<br/>

<u>Example:</u>
```java
@BoundObject(sourcePath = "http://my-server.com/config/auth_props",
             httpHeaders = { "Auth-Token: ${auth_token}",
                             "Other-Header: value",
                             "Cookie: favorite=1; permission=admin"}
            )
```
<br/>


### @BoundObject - trackingPolicy




<br/>


### @BoundObject - trackingInterval



<br/>


### @BoundObject - changeListener



<br/>


### @BoundObject - errorBehavior



<br/>


### @BoundProperty - customType / deserializationMethod


It doesn't know how your object should be treated.
<br/>


### @BoundProperty - errorBehavior



<br/>


### FactorySettingsBuilder



<br/>


### inheritance of parameters



<br/>



Its source code is shown next, followed by the compiler error seen when trying to compile this Rogue source code.
