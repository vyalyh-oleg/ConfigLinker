package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "http://localhost:8000/some.properties", sourceScheme = BoundObject.SourceScheme.HTTP, trackPolicy = BoundObject.TrackPolicy.ENABLE)
public interface HttpConfig {
	@BoundProperty(name = "com.my.prop.number")
	int getNumber();

	@BoundProperty(name = "com.my.prop.string")
	String getString();
}
