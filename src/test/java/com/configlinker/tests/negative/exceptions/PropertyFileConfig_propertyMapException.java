package com.configlinker.tests.negative.exceptions;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties")
public interface PropertyFileConfig_propertyMapException {

    @BoundProperty(name = "regexp.pattern", regexPattern = "?")
    String getRegexpPattern();

}
