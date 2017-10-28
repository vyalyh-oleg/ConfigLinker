package com.configlinker.tests.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyLoadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyLoadExceptionTest {

    @Test
    public void testPropertyValueNotFound() {
        Assertions.assertThrows(PropertyLoadException.class,
                () -> {
                    Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
                        add(PropertyFileConfig_fieldNotExist.class);
                    }};
                    ConfigSet configSet = ConfigSetFactory.create(interfaces);

                    PropertyFileConfig_fieldNotExist config = configSet.getConfigObject(PropertyFileConfig_fieldNotExist.class);
                    config.getNotExistField();
                });
    }

    @Test
    public void testIfPropertyFileNotFound() {
        Assertions.assertThrows(PropertyLoadException.class,
                () -> {
                    Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
                        add(PropertyFileConfig_fileNotExist.class);
                    }};
                    ConfigSet configSet = ConfigSetFactory.create(interfaces);

                    PropertyFileConfig_fileNotExist config = configSet.getConfigObject(PropertyFileConfig_fileNotExist.class);
                });
    }
}