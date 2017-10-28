package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class IntegerArrayTest {

//    @Test
    public void test22_getIntegerArray() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_getIntegerArray.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_getIntegerArray config = configSet.getConfigObject(PropertyFileConfig_getIntegerArray.class);
        int[] numbers = config.getNumbersArray();

        int expected = 333;
        int actual = numbers[2];

        Assertions.assertEquals(expected, actual, "Test to get int[] from configsFromClassPass file");
    }
}
