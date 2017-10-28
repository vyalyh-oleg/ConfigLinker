package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IntegerArrayTest {

    @Test
    public void test22_getIntegerArray() {
        TestRunner.LOGGER.info("start test22_getIntegerArray()");
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_getIntegerArray.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_getIntegerArray config = configSet.getConfigObject(PropertyFileConfig_getIntegerArray.class);
        int[] numbers = config.getNumbersArray();

        int expected = 333;
        int actual = numbers[2];

        Assert.assertEquals("Test to get int[] from configsFromClassPass file", expected, actual);
    }
}
