package com.configlinker.tests.positives;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class RegexpPatternTest {

    /**
     * <p>Example:
     * <br>@BoundProperty: <b>regexpPattern = "^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$"</b>
     * <br>properties: <b>email = qwerty132@test.com</b>
     * <p>Expectation:
     * <br>No exception, no output
     */
    @Test
    public void test_RegexpPatternEmail() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.add(PropertyFileConfig_regexpPattern.class);
        ConfigSet configSet = ConfigSetFactory.create(interfaces);

        PropertyFileConfig_regexpPattern config = configSet.getConfigObject(PropertyFileConfig_regexpPattern.class);
        String expectedResult = "qwerty132@test.com";
        String actualResult = config.getEmail();

        Assertions.assertEquals(expectedResult, actualResult);
    }


}
