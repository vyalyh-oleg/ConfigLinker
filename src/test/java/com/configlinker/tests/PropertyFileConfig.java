package com.configlinker.tests;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

import java.util.List;


@BoundObject(sourcePath = "my_app_main.properties", trackPolicy = BoundObject.TrackPolicy.ENABLE)
public interface PropertyFileConfig {

	@BoundProperty(name = "com.mycompany.info.basic.name")
	String getBasicName();

	@BoundProperty(name = "com.mycompany.info.basic.email", customType = String.class)
	List<String> getEmailList();

	@BoundProperty(name = "com.mycompany.info.basic.email")
	String[] getEmailArray();

	@BoundProperty(name = "key with space", customType = String.class)
	List<String> getKeyWithSpace();

	@BoundProperty(name = "! my text")
	String getWithSymbols();
}
