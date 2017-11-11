package com.configlinker.tests.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.FactoryConfigBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class IgnoreWhitespacesTest {

    @Test
    public void test23_ignoreWhitespaces() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_ignoreWhitespaces.class);
        }};

        ConfigSet configSet = ConfigSetFactory.create(interfaces, FactoryConfigBuilder.create().setIgnoreWhitespaces(false));

        PropertyFileConfig_ignoreWhitespaces config = configSet.getConfigObject(PropertyFileConfig_ignoreWhitespaces.class);
        String[] someValues = config.getColorsArray();

        String expected = "yellow";
        String actual = someValues[1];

        Assertions.assertEquals(expected, actual, "Test ignoring whitespaces");
    }

}
