package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "test_workdir/config_ignoreWhitespaces.properties")
public interface PropertyFileConfig_ignoreWhitespaces {

    //TODO ignore whitespaces element, #23
    @BoundProperty(name = "colors")
    String[] getColorsArray();

}
