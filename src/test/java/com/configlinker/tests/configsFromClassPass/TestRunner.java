package com.configlinker.tests.configsFromClassPass;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRunner {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestRunner.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("Welcome to Config-Linker Test\n");

        JUnitCore runner = new JUnitCore();
        Result result = runner.run(RunAllPropertyTests.class);

        LOGGER.info("end testing");
        LOGGER.info("Tests:   " + result.getRunCount());
        LOGGER.info("Failure: " + result.getFailureCount());
        LOGGER.info("Result:  " + result.wasSuccessful()+"\n");

        for (Failure failure : result.getFailures()) {
            LOGGER.error(failure.getTestHeader() + "\n" + failure.getTrace() + "\n");
        }


    }
}
