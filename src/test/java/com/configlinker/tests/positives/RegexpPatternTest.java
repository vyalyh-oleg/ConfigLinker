package com.configlinker.tests.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class RegexpPatternTest {

    @Test
    public void test_regexpPatternEmail() {
//        Pattern pattern = Pattern.compile("^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$", Pattern.CASE_INSENSITIVE);
        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_regexpPattern.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_regexpPattern config = configSet.getConfigObject(PropertyFileConfig_regexpPattern.class);
        String expectedResult = "qwerty132@test.com";
        String actualResult = config.getEmail();

        Assertions.assertEquals(expectedResult, actualResult);
    }


}
