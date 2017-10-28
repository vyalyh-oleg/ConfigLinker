package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "notExistFile.properties")
public interface PropertyFileConfig_fileNotExist {

    @BoundProperty(name = "name")
    String getName();

}
