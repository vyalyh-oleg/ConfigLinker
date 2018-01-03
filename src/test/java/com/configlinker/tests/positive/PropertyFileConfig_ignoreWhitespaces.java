package com.configlinker.tests.positive;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties")
public interface PropertyFileConfig_ignoreWhitespaces {

    @BoundProperty(name = "colors")
    String[] getColorsArray();

}
