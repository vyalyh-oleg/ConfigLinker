## Quick Start Guide (5 minutes)

#### Short Intro
**ConfigLinker** is a library designed to make it easier and more intuitive for developers to work with configuration files. 
It allows flexible mapping on particular Java types, standard collections (List, Set, Map), Enums, custom classes and all its combinations. It also has a lot of possibilities for making retrieving the parameters more flexible using arguments. It can watch for parameter change. 

The library can do other things, but let's take it in order, we only have 5 minutes now.

#### Code example (just copy-paste)
Below the simplest example is placed, which will help you to make the first step in understanding how it works.
Require 5 minutes for copy-paste, compile, run and understanding.
 
 
**Note:** _Put the properties files in the working directory (search path by default)._


**`(1)---------- File: credentials.properties ----------`**
```properties
user.authentication.name = Aragorn
user.authentication.password = aratorn

user.authorization.role = king
user.authorization.type = WARRIOR
user.authorization.rank = 3
user.authorization.accessClass = D
user.authorization.accessLevel = 5
```

**`(2)---------- File: credentials.properties ----------`**
```properties
server.zone1.type = web

```

**`(3)---------- File: UserCredentials.java ----------`**
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

**`(4)---------- File: UserType.java ----------`**
```java
public enum UserType
{
	WARRIOR, ARCHER, WIZARD
}
```

**`(5)---------- File: Server.java ----------`**
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

**`(6)---------- File: ConfigLinkerExample.java ----------`**
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
			System.out.println("Password: " + credentials.name());
			System.out.println("Role: " + credentials.userRole());
			System.out.println("Type: " + credentials.userType());
			System.out.println("Rank: " + credentials.userRank());
			System.out.println("Class: " + credentials.accessClass());
			System.out.println("Level: " + credentials.accessLevel());
		}
		
		// another configuration object can be retrieved from the same ConfigSet
		Server serverCfg = configs.getConfigObject(Server.class);
	}
}
```

#### Explanation of code.


 



