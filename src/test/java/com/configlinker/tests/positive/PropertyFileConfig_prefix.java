package com.configlinker.tests.positive;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties", propertyNamePrefix = "prefix.com")
public interface PropertyFileConfig_prefix {

    @BoundProperty(name = ".name")
    String getNamePrefix();
}
