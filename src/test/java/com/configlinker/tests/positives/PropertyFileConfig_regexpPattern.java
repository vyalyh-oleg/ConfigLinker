package com.configlinker.tests.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties")
public interface PropertyFileConfig_regexpPattern {

    @BoundProperty(name = "email", regexPattern = "^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$")
    String getEmail();
}
