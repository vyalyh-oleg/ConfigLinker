package com.configlinker.tests.positive;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class ArrayTest {

    /**
     * <p>Example of input values:
     * "numbers = <b>111,222,333</b>"
     * <p>Expectation:
     * <br>No exception, no output
     * <p>(issue: 22)
     */
    @Test
    public void test_GetIntegerArrayFromFileWithoutWhitespaces() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.add(PropertyFileConfig_getArray.class);
        ConfigSet configSet = ConfigSetFactory.create(interfaces);
        PropertyFileConfig_getArray config = configSet.getConfigObject(PropertyFileConfig_getArray.class);

        int[] numbers = config.getNumbersArray();

        int expected = 333;
        int actual = numbers[2];

        Assertions.assertEquals(expected, actual, "Test to get int[] from file");
    }

    /**
     * <p>Example of input values:
     * "colors = <b>white,yellow,blue</b>"
     * <p>Expectation:
     * <br>No exception, no output
     */
    @Test
    public void test_GetStringArrayFromFileWithoutWhitespaces() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.add(PropertyFileConfig_getArray.class);
        ConfigSet configSet = ConfigSetFactory.create(interfaces);
        PropertyFileConfig_getArray config = configSet.getConfigObject(PropertyFileConfig_getArray.class);

        String[] numbers = config.getColorsArray();

        String expected = "blue";
        String actual = numbers[2];

        Assertions.assertEquals(expected, actual, "Test to get String[] from file");
    }
}
