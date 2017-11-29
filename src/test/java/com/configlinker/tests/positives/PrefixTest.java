package com.configlinker.tests.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;

public class PrefixTest {

    /**
     * <p>If you set @BoundObject.propertyNamePrefix it will be added before this value,
     * <p>and in that case this value should begin with a dot.
     * <p>If it begins from any other acceptable symbols except dot,
     * <p>the value considered as full name and used without propertyNamePrefix.
     * <p>Example:
     * <p>@BoundObject: <b>propertyNamePrefix = "prefix.com"</b>
     * <p>@BoundProperty: <b>name = ".name"</b>
     * <p>property: <b>prefix.com.name = prefix</b>
     * <p>Expectation:
     * <br>No exception, no output
     * <p>(issue: 24)
     */
    @Test
    public void test_IdentifyPropertyNamePrefix() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.add(PropertyFileConfig_prefix.class);
        ConfigSet configSet = ConfigSetFactory.create(interfaces);
        PropertyFileConfig_prefix config = configSet.getConfigObject(PropertyFileConfig_prefix.class);

        String expected = "prefix";
        String actual = config.getNamePrefix();

        Assertions.assertEquals(expected, actual, "Test of identifing prefix name in properties");

    }
}
