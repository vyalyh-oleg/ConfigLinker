package com.configlinker.tests.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyMapExceptionTest {
    // task #26

    @Test
    //TODO change name of method
    public void test26_regexpPattern_inputWrongPatternThrowsPropertyMapException() {
        Assertions.assertThrows(PropertyMapException.class,
                () -> {
                    Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
                        add(PropertyFileConfig_propertyMapException.class);
                    }};
                    ConfigSet configSet = ConfigSetFactory.create(interfaces);

                    PropertyFileConfig_propertyMapException config = configSet.getConfigObject(PropertyFileConfig_propertyMapException.class);
                    config.getRegexPattern();

                });
    }
}
