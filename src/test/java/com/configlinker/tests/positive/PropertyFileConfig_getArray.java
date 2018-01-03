package com.configlinker.tests.positive;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "configs/common.properties")
public interface PropertyFileConfig_getArray {

    @BoundProperty(name = "numbersWithoutWhitespaces")
    int[] getNumbersArray();

    @BoundProperty(name = "colorsWithoutWhitespaces")
    String[] getColorsArray();


}
