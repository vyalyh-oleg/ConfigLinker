## Quick Start Guide (5 minutes)
<br/>

### Short Intro
**ConfigLinker** is a library designed to make it easier and more intuitive for developers to work with configuration files.  
It allows flexible mapping on particular Java types, standard collections (List, Set, Map), Enums, custom classes and all its combinations. It also has a lot of possibilities for making the parameters retrieving more flexible, f.e. allowing to use arguments. It can watch for parameter changes.

The library can do other things, but let's take it in order, we only have 5 minutes now.
<br/>


### Code example (just copy-paste)
Below the simplest example is placed, which will help you to make the first steps in understanding how it works.  
Require 5 minutes for copy-paste, compile, run and understanding.

**Note:** *Put the properties files in the working directory (search path by default).*
<br/>

**`---------- File: credentials.properties ----------`**
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn

user.authorization.role = king
user.authorization.type = WARRIOR
user.authorization.rank = 3
user.authorization.accessClass = D
user.authorization.accessLevel = 5
```

**`---------- File: credentials.properties ----------`**
```properties
server.zone1.types = web, file

```

**`---------- File: UserCredentials.java ----------`**
```java
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "credentials.properties")
public interface UserCredentials
{
	@BoundProperty(name = "user.authentication.name")
	String name();
	
	@BoundProperty(name = "user.authentication.password")
	String password();
	
	
	@BoundProperty(name = "user.authorization.role")
	String userRole();
	
	@BoundProperty(name = "user.authorization.type")
	UserType userType();
	
	@BoundProperty(name = "user.authorization.rank")
	short userRank();
	
	@BoundProperty(name = "user.authorization.accessClass")
	char accessClass();
	
	@BoundProperty(name = "user.authorization.accessLevel")
	short accessLevel();
}
```

**`---------- File: UserType.java ----------`**
```java
public enum UserType
{
	WARRIOR, ARCHER, WIZARD
}
```

**`---------- File: Server.java ----------`**
```java
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "server.properties")
public interface Server
{
	@BoundProperty(name = "server.zone1.type")
	String[] types();
}
```

**`---------- File: ConfigLinkerExample.java ----------`**
```java
public class ConfigLinkerExample
{
	public static void main(String[] args)
	{
		// lets create the set of our configurations
		ConfigSet configs = ConfigSetFactory.create(UserCredentials.class, Server.class);
		
		// now in any place of code where they are necessary you can retrieve desired configuration objects from the ConfigSet
		UserCredentials credentials = configs.getConfigObject(UserCredentials.class);
		
		// here is just an example of usage
		if (credentials.userRank() > 1 && credentials.userType() == UserType.WARRIOR)
		{
			System.out.println("Name: " + credentials.name());
			System.out.println("Password: " + credentials.password());
			System.out.println("Role: " + credentials.userRole());
			System.out.println("Type: " + credentials.userType());
			System.out.println("Rank: " + credentials.userRank());
			System.out.println("Class: " + credentials.accessClass());
			System.out.println("Level: " + credentials.accessLevel());
		}
		
		// another configuration object can be retrieved from the same ConfigSet
		Server serverCfg = configs.getConfigObject(Server.class);
		System.out.println(Arrays.toString(serverCfg.types()));
	}
}
```
<br/>


### Explanation of code
&rarr; Class **`ConfigSetFactory`**  
Is initial point in your code when you want to work with the library. It contains static methods which create and return `ConfigSet`.  

```
ConfigSetFactory.create(Class<?>... configInterfaces);
ConfigSetFactory.create(FactorySettingsBuilder builder, Class<?>... configInterfaces);
ConfigSetFactory.create(Set<Class<?>> configInterfaces);
ConfigSetFactory.create(FactorySettingsBuilder builder, Set<Class<?>> configInterfaces);
```
**`configInterfaces`**  
Interfaces annotated with `@BoundObject` and which methods (at least one) annotated with `@BoundProperty`. A set of interfaces should be a Set (so the duplicates are just ignored).
<br/>

**`FactorySettingsBuilder`** (will be discussed in more detail in next lessons)  
Builder, where you can preset parameters for `ConfigSetFactory`. It can be common properties for `@BoundObject` and `@BoundProperty` annotations, properties which tuning the behaviours on errors and so on.
<br/>

&rarr; Class **`ConfigSet`**  
It is the class which object you will use throughout you code for retrieving groups of configuration parameters. In other words <u>it contains a set of config groups</u>. In our case such a group of configuration parameters <u>is just an interface which is *bound* to a properties file</u>.
<br/>

&rarr; Annotation **`@BoundObject`**  
You should add this annotation to each interface that is a representation of configuration parameters from `properties` file.  
Multiple interfaces may be bound to the same `properties` file (f.e. if they logically separate its content).  
The *required parameter* is a `sourcePath`. It is full/relative path to the resource with properties (file / http link). If you put only the file name, it will be considered as residing in workdir (so `filename` equal to `./filename` which is equal to `<current_workdir>/filename`)
<br/>

&rarr; Annotation **`@BoundProperty`**  
Every method declaration in interface that expected to return the particular value from the `properties` file should have such an annotation.  
The *required parameter* is a `name`. It is property name for one record from `properties` file.  
E.g. property line `one.two.three = 123` has the name `one.two.three` and the value is `123`.
<br/>

