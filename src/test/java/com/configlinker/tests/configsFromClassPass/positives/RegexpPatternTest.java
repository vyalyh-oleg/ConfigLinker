package com.configlinker.tests.configsFromClassPass.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import com.configlinker.tests.configsFromClassPass.TestRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class RegexpPatternTest {

    @Test
    public void test_regexpPatternEmail() {
        TestRunner.LOGGER.info("start test_regexpPatternEmail()");
//        Pattern pattern = Pattern.compile("^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$", Pattern.CASE_INSENSITIVE);

        Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
            add(PropertyFileConfig_regexpPattern.class);
        }};
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_regexpPattern config = configSet.getConfigObject(PropertyFileConfig_regexpPattern.class);
        String expectedResult = "qwerty132@test.com";
        String actualResult = config.getEmail();

        Assert.assertEquals(expectedResult, actualResult);
    }


}
