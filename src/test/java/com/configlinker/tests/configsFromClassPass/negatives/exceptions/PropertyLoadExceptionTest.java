package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyLoadException;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyLoadExceptionTest {

    @Test(expected = PropertyLoadException.class)
    public void testPropertyValueNotFound() {
        TestRunner.LOGGER.info("start testPropertyValueNotFound()");
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_fieldNotExist.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_fieldNotExist config = configSet.getConfigObject(PropertyFileConfig_fieldNotExist.class);
        config.getNotExistField();
    }

    @Test(expected = PropertyLoadException.class)
    public void testIfPropertyFileNotFound() {
        TestRunner.LOGGER.info("start testIfPropertyFileNotFound()");
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_fileNotExist.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_fileNotExist config = configSet.getConfigObject(PropertyFileConfig_fileNotExist.class);
    }
}
