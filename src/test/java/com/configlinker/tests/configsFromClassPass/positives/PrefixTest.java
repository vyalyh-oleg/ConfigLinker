package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PrefixTest {

    @Test
    public void test24_namePrefix() {

//        TestRunner.LOGGER.info("start test24_namePrefix()");

//        If you set @BoundObject.propertyNamePrefix it will be added before this value,
//        and in that case this value should begin with a dot.
//        If it begins from any other acceptable symbols except dot,
//        the value considered as full name and used without propertyNamePrefix.
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_prefix.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_prefix config = configSet.getConfigObject(PropertyFileConfig_prefix.class);

        String expected = "prefix";
        String actual = config.getNamePrefix();

        Assert.assertEquals("Test name prefix - test24_namePrefix()", expected, actual);

    }
}
