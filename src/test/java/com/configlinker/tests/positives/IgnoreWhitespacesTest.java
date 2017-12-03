package com.configlinker.tests.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.FactoryConfigBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class IgnoreWhitespacesTest {


    /**
     * <p>Example of input values:
     * "colors = <b>white, yellow, blue</b>"
     * <p>Expectation:
     * <br>No exception, no output
     * <p>(issue: 23)
     */
    @Test
    public void test_IgnoreWhitespacesInPropertyValue() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.add(PropertyFileConfig_ignoreWhitespaces.class);
        ConfigSet configSet = ConfigSetFactory.create(interfaces);
        PropertyFileConfig_ignoreWhitespaces config = configSet.getConfigObject(PropertyFileConfig_ignoreWhitespaces.class);

        String[] someValues = config.getColorsArray();

        String expected = "yellow";
        String actual = someValues[1];

        Assertions.assertEquals(expected, actual, "Test of ignoring whitespaces in property value");
    }

}
