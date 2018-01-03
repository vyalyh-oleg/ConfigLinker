package com.configlinker.tests.negative.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyMapExceptionTest {

    /**
     * Examples of wrong regexPattern:
     * <br>"{", "*", "+", "(", "())", "?", "[", "\\"
     * <p>(issue: 26)
     */
    @Test
    public void test_InputWrongRegexpPatternThrowsPropertyMapException() {
        Assertions.assertThrows(PropertyMapException.class,
                () -> {
                    Set<Class<?>> interfaces = new HashSet<Class<?>>();
                    interfaces.add(PropertyFileConfig_propertyMapException.class);
                    ConfigSet configSet = ConfigSetFactory.create(interfaces);

                    PropertyFileConfig_propertyMapException config = configSet.getConfigObject(PropertyFileConfig_propertyMapException.class);
                    config.getRegexpPattern();

                });
    }
}
