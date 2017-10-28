package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "test_workdir/config_common.properties")
public interface PropertyFileConfig_fieldNotExist {

    @BoundProperty(name = "notExistField")
    String getNotExistField();

}
