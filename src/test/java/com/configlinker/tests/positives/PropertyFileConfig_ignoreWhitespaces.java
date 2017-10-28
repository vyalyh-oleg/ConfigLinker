package com.configlinker.tests.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "config_ignoreWhitespaces.properties")
public interface PropertyFileConfig_ignoreWhitespaces {

    //TODO ignore whitespaces element, #23
    @BoundProperty(name = "colors")
    String[] getColorsArray();

}
