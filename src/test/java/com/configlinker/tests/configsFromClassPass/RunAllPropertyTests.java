package com.configlinker.tests.configsFromClassPass;

import com.configlinker.tests.configsFromClassPass.negatives.exceptions.PropertyLoadExceptionTest;
import com.configlinker.tests.configsFromClassPass.negatives.exceptions.PropertyMapExceptionTest;
import com.configlinker.tests.configsFromClassPass.negatives.exceptions.PropertyNotFoundExceptionTest;
import com.configlinker.tests.configsFromClassPass.positives.IgnoreWhitespacesTest;
import com.configlinker.tests.configsFromClassPass.positives.IntegerArrayTest;
import com.configlinker.tests.configsFromClassPass.positives.PrefixTest;
import com.configlinker.tests.configsFromClassPass.positives.RegexpPatternTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //positive
        PrefixTest.class,
        RegexpPatternTest.class,
        IgnoreWhitespacesTest.class,
        IntegerArrayTest.class,
        //negative
        PropertyLoadExceptionTest.class,
        PropertyMapExceptionTest.class,
        PropertyNotFoundExceptionTest.class

})

public class RunAllPropertyTests {



}
