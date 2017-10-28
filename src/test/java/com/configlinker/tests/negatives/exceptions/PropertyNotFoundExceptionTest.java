package com.configlinker.tests.negatives.exceptions;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.exceptions.PropertyNotFoundException;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.Set;

public class PropertyNotFoundExceptionTest {

//TODO don't work
    // не получается вызвать нужное исключение

 //   @Test
    public void testErrorBehaviorThrowException() throws InterruptedException {
        Assertions.assertThrows(PropertyNotFoundException.class,
                () -> {
                    Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
                        add(PropertyFileConfig_errorBehaviorThrowException.class);
                    }};
                    ConfigSet configSet = ConfigSetFactory.create(interfaces);

                    PropertyFileConfig_errorBehaviorThrowException config = configSet.getConfigObject(PropertyFileConfig_errorBehaviorThrowException.class);

                    System.out.println(config.getValue());
     // за это время нужно изменить конфиг файл - например имя параметра
//                  Thread.sleep(10000);

                    System.out.println(config.getValue());
                });
    }
}

