package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "test_workdir/config_intArray.properties")
public interface PropertyFileConfig_getIntegerArray {

//    TODO not implemented yet, task #22
    @BoundProperty(name = "numbers")
    int[] getNumbersArray();


}
