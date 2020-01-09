### Quick Start Guide (+15 minutes)
*If you haven't read the previous QSG, please read it firstly. It will take you about 5 minutes. This part is continuation. Otherwise some explanations could be not understandable.*


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

arguments in methods

---

@BoundObject (2)
- sourceScheme (HTTP)
- trackingPolicy
- trackingInterval
- changeListener
- errorBehavior

@BoundProperty (2)
- customType
- deserializationMethod
- errorBehavior


FactorySettingsBuilder and inheritance

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
