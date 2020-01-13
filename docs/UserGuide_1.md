## User Guide 1
*If you haven't read the previous QSG, please read it firstly. It will take you about 5 minutes. This part is continuation. Otherwise some explanations may be not understandable.*

Here will be described advanced usages of annotations <b>`@BoundObject`</b> and <b>`@BoundProperty`.</b>

@BoundObject (1)
- charsetName
- sourceScheme (except HTTP and CONFIG_LINKER_SERVER)
- propertyNamePrefix
- whitespaces ?

@BoundProperty (1)
- complex types
- delimList
- delimKeyValue
- regex
- validator
- whitespaces

How to use arguments in methods

---

it doesn't know how your object should be treated



Its source code is shown next, followed by the compiler error seen when trying to compile this Rogue source code.


#### Variables in configuration parameters
It allows using arguments in methods to make the retrieving configuration parameters in more flexible way.  

**Note:** *This feature require that some `javac` compiler parameters are enabled (for proper reflection of interfaces during runtime).*


```
-parameters     Generate metadata for reflection on method parameters
-g              Generate all debugging info
```


#### Compatibility with java >=9
