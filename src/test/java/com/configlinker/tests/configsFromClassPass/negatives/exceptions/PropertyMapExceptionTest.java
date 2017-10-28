package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyMapException;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyMapExceptionTest {
    // task #26

    @Test(expected = PropertyMapException.class)
    //TODO change name of method
    public void test26_regexpPattern_inputWrongPatternShouldCallException(){
        TestRunner.LOGGER.info("start test26_regexpPattern_inputWrongPatternShouldCallException()");

        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_propertyMapException.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_propertyMapException config = configSet.getConfigObject(PropertyFileConfig_propertyMapException.class);
        config.getRegexPattern();

    }
}
