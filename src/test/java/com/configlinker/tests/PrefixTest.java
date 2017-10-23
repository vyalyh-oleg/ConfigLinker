package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "./my_app_main.properties", propertyNamePrefix = "pref")
public interface PrefixTest {
	@BoundProperty(name = ".one")
	String oneParam();

	@BoundProperty(name = ".two")
	String twoParam();
}
