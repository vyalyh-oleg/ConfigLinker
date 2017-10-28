package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyNotFoundException;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyNotFoundExceptionTest {

//TODO don't work
    // не получается вызвать нужное исключение

    @Test(expected = PropertyNotFoundException.class)
    public void testErrorBehaviorThrowException() throws InterruptedException {
        TestRunner.LOGGER.info("start testErrorBehavior THROW_EXCEPTION");
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_errorBehaviorThrowException.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_errorBehaviorThrowException config = configSet.getConfigObject(PropertyFileConfig_errorBehaviorThrowException.class);
        TestRunner.LOGGER.info(config.getValue());

        //за это время нужно изменить конфиг файл - например имя параметра
//        Thread.sleep(10000);

        TestRunner.LOGGER.info(config.getValue());
    }
}

