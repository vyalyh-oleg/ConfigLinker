package com.configlinker.tests.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "config_intArray.properties")
public interface PropertyFileConfig_getIntegerArray {

//    TODO not implemented yet, task #22
    @BoundProperty(name = "numbers")
    int[] getNumbersArray();


}
