## User Guide part 1
*If you didn't read the Quck Start Guide, please read it firstly. It will take you about 5 minutes. This part is continuation.  
Otherwise some explanations may be not understandable.*

Here will be described advanced usages of annotations <b>`@BoundObject`</b> and <b>`@BoundProperty`</b>.
<br/>

**`@BoundObject`**

- charsetName
- sourceScheme
- propertyNamePrefix
<br/>

**`@BoundProperty`**

- delimList
- delimKeyValue
- regex
- validator
- whitespaces
<br/>

It will also be told:

- Substitution Variables in configuration parameters
- how to use complex types like `Array`, `List`, `Set`, `Map`;
- how to use arguments in property retrieving methods (for parameterized query of parameter's value);
<br/>



### @BoundObject - charsetName

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


### @BoundObject - sourceScheme (CLASSPATH, FILE, HTTP)

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


### @BoundObject - propertyNamePrefix

The common names' part of parameters group that is used to bind with methods of this annotated interface.  
If it is not specified you can use only full parameter names in `BoundProperty.name()`.  
If it has any value then both variants (full and prefix-aware) can be used.  

For example, if the prefix  is set to `"mycompany"`, then the `@BoundProperty.name()` can be as `".serverName"` (start with dot is obligatory).  
This means, the final parameter name, which will be searched in property file, is `"mycompany.serverName"`.  

Without dot at the beginning `@BoundProperty.name()` is considered as full parameter name and the prefix is not taken into account.
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


### Substitution Variables in configuration parameters

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

It allows using arguments in methods to make the configuration methods more flexible.
<br/>

**Note:** *This feature require that some `javac` compiler parameters are enabled (for proper reflection of interfaces during runtime).*


```
-parameters     Generate metadata for reflection on method parameters
-g              Generate all debugging info
```



<br/>


### Complex types: `Array[]`, `List<>`, `Set<>`, `Map<>`
### @BoundProperty - delimList / delimKeyValue

These parts combined to the one.
<br/>

**`delimList`**  
Delimiter for parameter value, which is treated as `List<>` or key-value pairs in `Map<>`. Default value is comma - `','`.  
<u>Example:</u> `param.name.in.file = one,two,three`

**`delimKeyValue`**  
Delimiter between key and value for `Map<>`. Default value is colon - `':'`.  
<u>Example:</u> `param.name.in.file = color:red,number:two,target:method`

<br/>
Lets modify the example from the previous *QuickStartGuide*.  
Please, see **description in comments**.  
<br/>

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


### @BoundProperty - whitespaces

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


### @BoundProperty - regex

Regex pattern for checking raw text values which were read from property files.  
For simple types single value is checked, and for collection types (arryas, list, map, enum) **every element value** (as string) is checked.  
By defaults regex check does not used.  

If validation fails the `PropertyMatchException` will be thrown.  
Its message and the hierarchy of 'cause' contains additional information about error.

The checking process perfroms on the call of `ConfigSetFactory.create()` method.  
*The only exception* is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
<br/>

<u>Example:</u>
```
@BoundObject(sourcePath = "configs/mailing.properties")
interface MailingConfig
{
	String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	@BoundProperty(name = "workgroup.emails", regex = emailPattern)
	List<String> emails();
}
```
<br/>


### @BoundProperty - validator

Custom validator for returned value. Validate configuration value in their object form.  
By default validators are not used.  
If you need additional checks just implement `IPropertyValidator` interface and point class here.

If validation fails the `PropertyValidateException` will be thrown.  
Its message and the hierarchy of 'cause' contains additional information about error.

The checking process perfroms on the call of `ConfigSetFactory.create()` method.  
*The only exception* is when your configuration methods contain parameters, therefore its values could be checked only during runtime, when the actual arguments will be passed to the method.
<br/>

<u>Example:</u>
```
class EmailDomainValidator implements IPropertyValidator<String>
{
	@Override
	public void validate(String value) throws PropertyValidateException
	{
		if (!value.endsWith("@physics.ua"))
			throw new PropertyValidateException("'" + value + "' not in 'physics.ua' domain.");
	}
}

class EmailMapDomainValidator implements IPropertyValidator<Object[]>
{
	@Override
	public void validate(Object[] value) throws PropertyValidateException
	{
		// value[0] -- key, and is always String
		// value[1] -- value, could be any object depending from return type in interface method
		
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
<br/>
