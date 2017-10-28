package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;

public class PrefixTest {

    @Test
    public void test24_namePrefix() {
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

        Assertions.assertEquals(expected, actual, "Test name prefix");

    }
}
