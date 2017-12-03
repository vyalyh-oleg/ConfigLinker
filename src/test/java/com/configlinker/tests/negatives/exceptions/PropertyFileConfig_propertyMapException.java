package com.configlinker.tests.negatives.exceptions;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties")
public interface PropertyFileConfig_propertyMapException {

    @BoundProperty(name = "regexp.pattern", regexpPattern = "?")
    String getRegexpPattern();

}
