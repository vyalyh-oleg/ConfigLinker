## User Guide part 1
*If you didn't read the Quck Start Guide, please read it firstly. It will take you about 5 minutes. This part is continuation.  
Otherwise some explanations may be not understandable.*

Here will be described advanced usages of annotations <b>`@BoundObject`</b> and <b>`@BoundProperty`</b>.
<br/>

**`@BoundObject`**

- charsetName
- sourceScheme
- propertyNamePrefix


**`@BoundProperty`**

- delimList
- delimKeyValue
- regex
- validator
- whitespaces

It will also be told:

- how to use complex types like Array, List, Set, Map;
- how to use arguments in property retrieving methods (for parameterized query of parameter value);
<br/>



### @BoundObject - charsetName

It says what charset to use during loading raw text.  
The default value in annotation is empty string, meaning that particular value will be taken from `FactorySettingsBuilder` (described in *User Guide part 2*), and it is `StandardCharsets.UTF_8`.  
The charset object is retrieving by the call `Charset.forName(String)`, so make sure that the support of it is present in your environment.
<br/>

<u>Example:</u>

```java
@BoundObject(sourcePath = "credentials.properties", charsetName = "UTF-16")
public interface UserCredentials
{
  // ...
}
```


### @BoundObject - sourceScheme (CLASSPATH, FILE, HTTP)

Describe the type of the source that is used to retrieve property values for annotated object.

- `SourceScheme.INHERIT`  
  Mean that concrete value will be get from `FactorySettingsBuilder`, and it is `SourceScheme.FILE` by default.

- `SourceScheme.CLASSPATH`  
  Use if the property file resides in the classpath of running VM.  
  The `BoundObject.sourcePath()` should be relative.

- `SourceScheme.FILE`  
  Use if the property file should be loaded from one of mounted file systems.  
  The `BoundObject.sourcePath()` should be absolute or relative.

- `SourceScheme.HTTP`  
  Use if the `BoundObject.sourcePath()` was set as valid URL to load file from HTTP/S server.  
  The usage is discussed in the *User Guide part 2*

- `SourceScheme.CONFIG_LINKER_SERVER`  
  It is not implemented yet.
<br/>


### @BoundObject - propertyNamePrefix

The common names' part of parameters group that is used to bind with methods of this annotated interface.  
If it is not specified you can use only full parameter names in `BoundProperty.name()`.  
If it has any value then both variants (full and prefix-aware) can be used.  

For example, if the prefix  is set to `"mycompany"`, then the `@BoundProperty.name()` can be as `".serverName"` (start with dot is obligatory).  
This means, the final parameter name, which will be searched in property file, is `"mycompany.serverName"`.  

Without dot at the beginning `@BoundProperty.name()` is considered as full parameter name and the prefix is not taken into account.
<br/>

<u>Example:</u>

**`properties:`**
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn
user.authentication.email = aragorn@gondor.middleearth
user.maxhealth = 1000
```

Here the first three parameters use prefix and the fourth parameter has absolute name.

**`code:`**
```java
@BoundObject(sourcePath = "credentials.properties",
             propertyNamePrefix = "user.authentication")
public interface UserCredentials
{
	@BoundProperty(name = ".name")
	String name();
	
	@BoundProperty(name = ".password")
	String password();
	
	@BoundProperty(name = ".email")
	String email();
	
	@BoundProperty(name = "user.maxhealth")
	int maxHealth();
}

```
<br/>

**global variables** for substitution

Prefix can also contain **${variables}** for substituting some its parts.  
The name of variable can consist of letters, numbers and underscores.  
Variables should be set with `FactorySettingsBuilder.addParameter(String, String)`.  

<u>Example:</u>

If annotation is  
`@BoundObject(propertyNamePrefix = "servers.${type}.srv1.configuration")`  

where `${type}` can be `"test"` or `"production"`, etc.

then  
`FactorySettingsBuilder.addParameter("type", "production")`  

results in  
`"servers.production.srv1.configuration"`  
<br/>


### @BoundProperty - delimList / delimKeyValue


---



### @BoundProperty - regex



### @BoundProperty - validator



### @BoundProperty - whitespaces



### Complex types (Array, List, Set, Map)



### Arguments in property retrieving methods



**`---------- File: credentials.properties ----------`**
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn

user.authorization.role = king, sword_master
user.authorization.type = WARRIOR
user.authorization.rank = 3
user.authorization.accessClassAndLevel = D:5, R:2
```

**`---------- File: credentials.properties ----------`**


It doesn't know how your object should be treated.



Its source code is shown next, followed by the compiler error seen when trying to compile this Rogue source code.


#### Variables in configuration parameters
It allows using arguments in methods to make the retrieving configuration parameters in more flexible way.  

**Note:** *This feature require that some `javac` compiler parameters are enabled (for proper reflection of interfaces during runtime).*


```
-parameters     Generate metadata for reflection on method parameters
-g              Generate all debugging info
```


#### Compatibility with java >=9
