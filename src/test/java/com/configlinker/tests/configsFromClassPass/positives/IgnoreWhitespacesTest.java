package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IgnoreWhitespacesTest {

    @Test
    public void test23_ignoreWhitespaces() {
        TestRunner.LOGGER.info("start test23_ignoreWhitespaces()");
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_ignoreWhitespaces.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_ignoreWhitespaces config = configSet.getConfigObject(PropertyFileConfig_ignoreWhitespaces.class);
        String[] someValues = config.getColorsArray();

        String expected = "yellow";
        String actual = someValues[1];

        Assert.assertEquals("Test ignoring whitespaces", expected, actual);
    }


}
