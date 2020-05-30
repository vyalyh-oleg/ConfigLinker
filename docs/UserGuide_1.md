# User Guide part 1

Other articles:

- [ReadMe](../README.md)
- [Quick Start Guide (5 minutes)](QuickStartGuide_5m.md)
- [User Guide part 2](UserGuide_2.md)
<br/>

*If you didn't read the **Quick Start Guide**, please read it firstly. It will take you about 5 minutes. This part is continuation.  
Otherwise some explanations may be not understandable.*
<br/>

[Public API classes short description.](#public-api-classes)

[Automatically supported types.](#automatically-supported-return-types)

Here the advanced usages of annotations <b>`@BoundObject`</b> and <b>`@BoundProperty`</b> will be also described.

**`@BoundObject`**

- [sourceScheme](#boundobjectsourcescheme-classpath-file-http)
- [sourcePath](#boundobjectsourcepath)
- [defaultSourcePath](#boundobjectdefaultsourcepath)
- [charsetName](#boundobjectcharsetname)
- [propertyNamePrefix](#boundobjectpropertynameprefix-boundpropertyname)
<br/>

**`@BoundProperty`**

- [name](#boundobjectpropertynameprefix-boundpropertyname)
- [delimList](#complex-types-array-list-set-map-and-boundpropertydelimlistdelimkeyvalue)
- [delimKeyValue](#complex-types-array-list-set-map-and-boundpropertydelimlistdelimkeyvalue)
- [regex](#boundpropertyregex)
- [validator](#boundpropertyvalidator)
- [whitespaces](#boundpropertywhitespaces)
<br/>

It will also be told about:

- [variables substitution in configuration parameters;](#variables-substitution-in-configuration-parameters)
- [how to use complex types like `Array`, `List`, `Set`, `Map`;](#complex-types-array-list-set-map-and-boundpropertydelimlistdelimkeyvalue)
- [how to use arguments in property retrieving methods (for parameterized query of parameter's value);](#arguments-in-configuration-methods)
- [Compatibility with java >=9](#compatibility-with-java-9)
<br/>


### Public API classes

Short description of all classes that you'll use.
<br/>

**`@BoundObject`**  
You should add this annotation to each interface that is a representation of configuration parameters from `properties` file.

**`@BoundProperty`**  
Every method declaration in interface that expected to return the particular value from the `properties` file should have such an annotation.
<br/>

**`ConfigSetFactory`**  
Is initial point in your code when you want to work with the library. It contains static methods which create and return `ConfigSet` instanes.

`FactorySettingsBuilder`  
Builder, where you can preset settings for `ConfigSetFactory`. These can be common parameters for  `BoundObject` and `BoundProperty` annotations.

**`ConfigSet`**  
Is the class which object you will use throughout you code for retrieving groups of configuration parameters. In other words <u>it contains a set of config groups</u>. In our case such a group of configuration parameters <u>is just an interface which is <b>bound</b> to a properties file (or other source).</u>
<br/>

**`IConfigChangeListener`**  
Implement this interface and point your class in annotation `BoundObject#changeListener()` for receiving notifications on configuration updates.

**`ConfigChangedEvent`**  
Event which is passed as an argument in `IConfigChangeListener.configChanged`.
<br/>

**`IPropertyValidator`**  
For creation custom validators for returned value. Validates configuration value in their object form.

**`IDeserializer`**  
If you want to use custom return type, you must just implement deserialization logic for it and point here deserializer class.

<br/>


### Automatically supported return types

So you no need additional configuration in annotations if you use them:

Basic types:

- all primitives;
- all wrappers of primitives;
- `String`;
- `Enum`;

Arrays and Collections:

- arrays of primitives;
- arrays of wrappers (of primitives);
- `String[]`;
- `Enum[]`
- `List<String>`;
- `Set<String>`;
- `Map<String,String>`;

Extended types:

- `URL`
- `URI`
- `UUID`
- `InetAddress`

See also: [Predefined deserializers](UserGuide_2.md#predefined-deserializers) for `Date` type.

```properties
# example of parameters with extended types usage

type.URL = http://mysite.com:8080/path/to;matrix_1=foo;matrix_2=bar/resource.jsf?one=1&two=2#firstpart
type.URI.1 = file:///home/john/Documents/charts.pdf
type.URI.2 = mailto:John.Doe@example.com
type.URI.3 = ldap://[2001:db8::7]/c=GB?objectClass?one
type.URI.4 = urn:oasis:names:specification:docbook:dtd:xml:4.1.2
type.UUID = 1711e708-a486-4dc5-8dbe-f275b601064f
type.InetAddress.IPv4 = 192.168.12.10
type.InetAddress.IPv6.1 = ::123
type.InetAddress.IPv6.2 = ::ffff
type.InetAddress.IPv6.3 = fe80::fc02:bcff:fea3:919b
type.InetAddress.HostName = mysite.com
```
<br/>


### @BoundObject.sourceScheme (CLASSPATH, FILE, HTTP)

Describe the type of the source that is used to retrieve property values for annotated object.

- `SourceScheme.INHERIT`  
  Mean that concrete value will be get from `FactorySettingsBuilder`, and it is `SourceScheme.FILE` by default.

- `SourceScheme.FILE`  
  Use if the property file should be loaded from one of mounted file systems.  
  The `BoundObject.sourcePath()` should be absolute or relative.

- `SourceScheme.CLASSPATH`  
  Use if the property file resides in the classpath of running VM.  
  The `BoundObject.sourcePath()` should be relative.

- `SourceScheme.HTTP`  
  Use if the `BoundObject.sourcePath()` was set as valid URL to load file from HTTP/S server.  
  The usage is discussed in the *User Guide part 2*

- `SourceScheme.CONFIG_LINKER_SERVER`  
  It is not implemented yet.
<br/>


### @BoundObject.sourcePath

It is *required parameter*. It describes full/relative path to the resource with properties (file / http link). If you put only the file name, it will be considered as residing in the working directory of your program (so the value `filename` will be equal to `./filename` which is equal to `<current_workdir>/filename`)
```java
@BoundObject(sourcePath = "credentials.properties", sourceScheme = SourceScheme.FILE)
public interface UserCredentials
{
  // ...
}
```
<br/>


### @BoundObject.defaultSourcePath
In case if you got used to default values, and do not want to put the entire set of parameters in outer configuration file, you may create special `property` files which are consist of default parameters.  
Such files should be placed near the config interface, so they'll become the part of code (part of jar file) after compilation and assembling.

It is a good design to separate the code from the data, even for default props.

<u>Example:</u>
```java
@BoundObject(sourcePath = "configs/server.properties", defaultSourcePath = "server.default.properties",
    errorBehavior = ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION)
interface ErrorBehavior_WithDefaultsAndException_ReturnDefaults
{
  // ...
}
```
Here you may see another special `@BoundObject.errorBehavior` parameter.  
In case of usage `defaultSourcePath` it should be set as `ErrorBehavior.TRY_DEFAULTS_OR_EXCEPTION` or `ErrorBehavior.TRY_DEFAULTS_OR_NULL`.  
For more details see: [@BoundObject.errorBehavior](UserGuide_2.md#boundobjecterrorbehavior)
<br/>


### @BoundObject.charsetName

It says what charset to use during loading raw text.  
The default value in annotation is empty string, meaning that particular value will be taken from `FactorySettingsBuilder` (described in *User Guide part 2*), and it is `StandardCharsets.UTF_8`.  
The charset object is retrieving by the call `Charset.forName(String)`, so make sure that the support of it presents in your environment.
<br/>

<u>Example:</u>

```java
@BoundObject(sourcePath = "credentials.properties", charsetName = "UTF-16")
public interface UserCredentials
{
  // ...
}
```
<br/>


### @BoundObject.propertyNamePrefix, @BoundProperty.name

`propertyNamePrefix` is the *common part* of a group of parameter names in property file. This part is used for construction the full name, which is used for binding methods in annotated interface to the specific key in `property` file.

`@BoundProperty.name` is the required parameter. It is the key name for one record from property file.
If `@BoundObject.propertyNamePrefix` is not specified you can use only full names in `BoundProperty.name`.  
If `propertyNamePrefix` has any value then both variants (full and prefix-aware) of names can be used.  

For example, if the prefix  is set to `"mycompany"`, then the `@BoundProperty.name` can look as `".serverName"` (starting with the dot is obligatory). This means, the final parameter name, which will be searched in property file is `"mycompany.serverName"`.  

Without the dot at the beginning the `@BoundProperty.name` is considered as full name and the `propertyNamePrefix` is not taken into account.
<br/>

<u>Example:</u>

*`---------- properties ----------`*
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn
user.authentication.email = aragorn@gondor.middleearth
user.maxhealth = 1000
```

Here the first three parameters use prefix and the fourth parameter has absolute name.

*`---------- code ----------`*
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
    
    // here we use absolute name along with relatives
	@BoundProperty(name = "user.maxhealth")
	int maxHealth();
}

```
<br/>


### Variables substitution in configuration parameters

It is possible to use **global variables** like **`${variables}`** for substitution in:

- @BoundObject - sourcePath  
- @BoundObject - httpHeaders  
- @BoundObject - propertyNamePrefix  
- @BoundProperty - name  

The **name of variable** can consist of letters, numbers and underscores.  
Variables should be set with `FactorySettingsBuilder.addParameter(String, String)`.  
<br/>

<u>Example:</u>

If annotation is  
`@BoundObject(propertyNamePrefix = "servers.${type}.srv1.configuration")`  

then  
`FactorySettingsBuilder.addParameter("type", "production")`  

results in property name like  
`"servers.production.srv1.configuration"`

<br/>


### Arguments in configuration methods

It is allowed to use arguments in methods to make the configuration retrieving more flexible.
<br/>

**Note:** *This feature requires that some `javac` compiler parameters are enabled (for proper reflection of interfaces during runtime).*

```
-parameters     Generate metadata for reflection on method parameters
```

This functionality is tightly related to the parameter `@BoundProperty.name`.

So, let's tell about the `name`.

It should contain string representation of property name that is used in configuration.  

It can be either global or relative (where `propertyNamePrefix` is used).  

You can use variables like `${variables}` for substituting some parts of the name.  

Name can also contain **dynamic variables** which can be used in runtime for method arguments.  
For using this ability you must declare the method that accepts desired number of `String` or `Enum` arguments.  

<u>Example:</u>  
Let's imagine we have quite big limit configurations for all our servers
```properties
# for limits configuration

server.DataCenter-1.master.configuration.memory.limit.min = 
server.DataCenter-1.master.configuration.memory.limit.max = 
server.DataCenter-1.master.configuration.memory.limit.default = 

server.DataCenter-1.slave.configuration.memory.limit.min = 
server.DataCenter-1.slave.configuration.memory.limit.max = 
server.DataCenter-1.slave.configuration.memory.limit.default = 


server.DataCenter-1.master.configuration.disk.limit.min = 
server.DataCenter-1.master.configuration.disk.limit.max = 
server.DataCenter-1.master.configuration.disk.limit.default = 

server.DataCenter-1.slave.configuration.disk.limit.min = 
server.DataCenter-1.slave.configuration.disk.limit.max = 
server.DataCenter-1.slave.configuration.disk.limit.default = 


server.DataCenter-1.master.configuration.cpu.limit.min = 
server.DataCenter-1.master.configuration.cpu.limit.max = 
server.DataCenter-1.master.configuration.cpu.limit.default = 

server.DataCenter-1.slave.configuration.cpu.limit.min = 
server.DataCenter-1.slave.configuration.cpu.limit.max = 
server.DataCenter-1.slave.configuration.cpu.limit.default = 


# for notification parameters
server.DataCenter-1.master.notification.frequency = 
server.DataCenter-1.master.notification.emails = 

server.DataCenter-1.slave.notification.frequency = 
server.DataCenter-1.slave.notification.emails = 


# and now imagine that there are other servers in another data center exist
# thus we'll require the second, third, etc. bocks of configurations like

#server.DataCenter-2...
#server.DataCenter-3...

# and so on
```
Solution:
```java
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

import java.util.List;

@BoundObject(sourcePath = "server.properties", propertyNamePrefix = "server.${dataCenter}.@{type}")
public interface Server
{
	@BoundProperty(name = ".configuration.@{group}.limit.@{border}")
	int limitFor(ServerType type, ServerResource group, LimitType border);
	
	@BoundProperty(name = ".notification.frequency")
	int notificationFrequency(ServerType type);
	
	@BoundProperty(name = ".notification.emails")
	List<String> notificationEmails(ServerType type);
}

enum ServerType { master, slave; }

enum ServerResource { memory, disk, cpu; }

enum LimitType { min, max; }
```
Usage:
```java
public class ConfigLinkerExample
{
	public static void main(String[] args)
	{
		FactorySettingsBuilder settingsBuilder = FactorySettingsBuilder.create()
				.addParameter("dataCenter", "DataCenter-1")
				.addParameter("anotherVar", "value");
		ConfigSet configs = ConfigSetFactory.create(settingsBuilder, Server.class);

		// retrieving the actual configuration parameters		
		Server serverCfg = configs.getConfigObject(Server.class);

		int masterMemoryMin = serverCfg.limitFor(ServerType.master, ServerResource.memory, LimitType.min);
		System.out.println("masterMemoryMin: " + masterMemoryMin);
		
		List<String> notificationEmails = serverCfg.notificationEmails(ServerType.slave);
		int notificationFrequency = serverCfg.notificationFrequency(ServerType.slave);
	}
}
```
<br/>


### Complex types `Array[]`, `List<>`, `Set<>`, `Map<>` and @BoundProperty.delimList/delimKeyValue

These parts will be more convenient to consider together.
<br/>

**`delimList`**  
Delimiter for parameter raw value, which is treated as enumeration values for `List<>` or key-value pairs in `Map<>`. Default delimiter is comma - `','`.  
<u>Example:</u> `param.name.in.file = one,two,three`

**`delimKeyValue`**  
Delimiter between key and value in one pair for `Map<>`. Default delimiter is colon - `':'`.  
<u>Example:</u> `param.name.in.file = color:red,number:two,target:method`
<br/>

Lets modify the example from the previous *QuickStartGuide*.  
Please, see **description in comments**.  

*`---------- File: credentials.properties ----------`*
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn

user.authorization.roles = king, sword_master
user.authorization.type = WARRIOR
user.authorization.rank = 3
# let's use nonstandard delimiters: '-' for key-value and ';' for pairs enumeration
user.authorization.accessClassesAndLevels = D-5; R-2

# here '/' separates values
user.authorization.salt = 18/45/45/23/32
```

*`---------- File: UserCredentials.java ----------`*
```java
import net.crispcode.configlinker.annotations.BoundObject;
import net.crispcode.configlinker.annotations.BoundProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;

@BoundObject(sourcePath = "credentials.properties")
public interface UserCredentials
{
	@BoundProperty(name = "user.authentication.name")
	String name();
	
	@BoundProperty(name = "user.authentication.password")
	String password();
	
	// here we use default delimiter, so there is no need of changes
	// if the same values ​​are found, the first one will be accepted, and all similars will be ignored
	@BoundProperty(name = "user.authorization.roles")
	Set<String> userRoles();
	
	@BoundProperty(name = "user.authorization.type")
	UserType userType();
	
	@BoundProperty(name = "user.authorization.rank")
	short userRank();

	// here we declare delimiter characters used in properties file
	// if duplicate keys are found, only the first pair (from every duplicate list) will be accepted
	@BoundProperty(name = "user.authorization.accessClassesAndLevels", delimList = ";", delimKeyValue = "-")
	Map<Character, Integer> accessClasses();
	
	// arrays and lists can contain duplicate values
	@BoundProperty(name = "user.authorization.salt", delimList = "/")
	short[] generationSaltAsArray();
	
	// and it is permitted to use the same parameter for different mappings
	@BoundProperty(name = "user.authorization.salt", delimList = "/")
	List<Short> generationSaltAsList();
}
```

*`---------- File: ConfigLinkerExample.java ----------`*
```java
import net.crispcode.configlinker.ConfigSet;
import net.crispcode.configlinker.ConfigSetFactory;

import java.util.Arrays;

public class ConfigLinkerExample
{
	public static void main(String[] args)
	{
		ConfigSet configs = ConfigSetFactory.create(UserCredentials.class, Server.class);
		
		UserCredentials credentials = configs.getConfigObject(UserCredentials.class);
		
		if (credentials.userRank() > 1 && credentials.userType() == UserType.WARRIOR)
		{
			System.out.println("Name: " + credentials.name());
			System.out.println("Password: " + credentials.password());
			System.out.println("Role: " + credentials.userRoles());
			System.out.println("Type: " + credentials.userType());
			System.out.println("Rank: " + credentials.userRank());
			System.out.println("Classes and Levels: " + credentials.accessClasses());
			System.out.println("Salt from List: " + credentials.generationSaltAsList().getClass());
			System.out.println("Salt from List: " + Arrays.toString(credentials.generationSaltAsList().toArray()));
			System.out.println("Salt from Array: " + credentials.generationSaltAsArray().getClass());
			System.out.println("Salt from Array: " + Arrays.toString(credentials.generationSaltAsArray()));
		}
		
		Server serverCfg = configs.getConfigObject(Server.class);
		System.out.println("Server types: " + Arrays.toString(serverCfg.types()));
	}
}

```
<br/>


### @BoundProperty.whitespaces

Indicates whether or not to ignore leading and trailing getWhitespaces for configuration names and values.  
This behaviour concerns single parameter values, every value in lists declarations, every key and value in maps declarations.

The default value is `Whitespaces.INHERIT`, which means it takes the value from `FactorySettingsBuilder.setWhitespaces()`.  
But the actual behaviour can be overriden for every property.
<br/>

<u>Examples:</u>  
(1)  
`'color = green '`  
if ignore: value is `"green"`  
if not ignore: value is `"green "`  

(2)  
`'color = green, blue '`  
if ignore: values is `"green"` and `"blue"`  
if not ignore: values is `"green"` and `" blue "`  

(3)  
`'color = one: green, two :blue ,three : red '`  
if ignore: keys/values are `"one":"green"`, `"two":"blue"`, `"three":"red"`  
if not ignore: keys/values are `"one":" green"`, `" two ":"blue "`, `"three ":" red "`

<br/>


### @BoundProperty.regex

Regex pattern for checking raw text values which were read from property files.  
For simple types single value is checked, and for collection types (arryas, list, map, enum) **every element value** (as string) is checked.  
By defaults regex check does not used.  

If validation fails the `PropertyMatchException` will be thrown.  
Its message and the hierarchy of 'cause' contain additional information about error.

The checking process performs on the call of `ConfigSetFactory.create()` method.  
*The only exception* is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
<br/>

<u>Example:</u>
```properties
workgroup.emails = vitaliy.mayko@physics.ua, zinovij.nazarchuk@physics.ua, mark.gabovich@physics.ua
```

```java
@BoundObject(sourcePath = "configs/mailing.properties")
interface MailingConfig
{
	String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	@BoundProperty(name = "workgroup.emails", regex = emailPattern)
	List<String> emails();
}
```
<br/>


### @BoundProperty.validator

Custom validator for returned value. Validates configuration value in their object form.  
By default validators are not used.  
If you need additional checks just implement `IPropertyValidator` interface and point class here.

If validation fails the `PropertyValidateException` will be thrown.  
Its message and the hierarchy of 'cause' contain additional information about error.

The checking process performs on the call of `ConfigSetFactory.create()` method.  
*The only exception* is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
<br/>

<u>Example:</u>
```properties
workgroup.emails = vitaliy.mayko@physics.ua, zinovij.nazarchuk@physics.ua, mark.gabovich@physics.ua
```

```java
class EmailDomainValidator implements IPropertyValidator<String>
{
	@Override
	public void validate(String value) throws PropertyValidateException
	{
		if (!value.endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value + "' not in 'physics.ua' domain.");
	}
}


// if the property is a Map
class EmailMapDomainValidator implements IPropertyValidator<Object[]>
{
	@Override
	public void validate(Object[] value) throws PropertyValidateException
	{
		// value[0] -- key, and is always String
		// value[1] -- value, could be any object, depending from the return type in interface method
		
		if (!((String) value[1]).endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value[1] + "' for key '" + value[0] + "' not in 'physics.ua' domain.");
	}
}

@BoundObject(sourcePath = "configs/mailing.properties")
interface CustomValidator
{
	@BoundProperty(name = "workgroup.email", validator = EmailDomainValidator.class)
	String email();

	@BoundProperty(name = "workgroup.emails.map", validator = EmailMapDomainValidator.class)
	Map<String, String> emailsMap();
}
```
<br/>


### Compatibility with java >=9
Should be compatible if you permit the access for this library to the module where your configuration interfaces reside.
<br/>
