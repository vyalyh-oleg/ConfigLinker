### Quick Start Guide (5 minutes)

**ConfigLinker** is a library designed to make it easier and more intuitive for developers to work with configuration files. 
It allows flexible mapping on particular Java types, standard collections (List, Set, Map), Enums, custom classes and all its combinations.

Below the simplest example is placed, which will help you to make the first step in understanding how it works.
Require 5 minutes for copy-paste, compile and run.

<br> 

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


**`---------- File: UserCredentials.java ----------`**
```java
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "")
public interface UserCredentials
{
	@BoundProperty(name = "user.authentication.name")
	String name();
	
	@BoundProperty(name = "user.authentication.password")
	String password();
	
	
	@BoundProperty(name = "user.authorization.role")
	String userRole();
	
	@BoundProperty(name = "user.authorization.type")
	Type userType();
	
	@BoundProperty(name = "user.authorization.rank")
	short userRank();
	
	@BoundProperty(name = "user.authorization.accessClass")
	char accessClass();
	
	@BoundProperty(name = "user.authorization.accessLevel")
	char accessLevel();
}

enum Type
{
	WARRIOR, ARCHER, WIZARD
}
```

Necessary java compiler parameters:
-g -parameters


Compatible with java >=9

 



