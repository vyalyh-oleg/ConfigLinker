package com.configlinker.tests.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "config_prefix.properties", propertyNamePrefix = "prefix.com")
public interface PropertyFileConfig_prefix {

    //task #24
    @BoundProperty(name = ".name")
    String getNamePrefix();
}
