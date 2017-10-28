package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.ErrorBehavior;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;


@BoundObject(sourcePath = "test_workdir/config_errorBehaviorThrowException.properties", errorBehavior = ErrorBehavior.THROW_EXCEPTION, trackPolicy = BoundObject.TrackPolicy.ENABLE)
public interface PropertyFileConfig_errorBehaviorThrowException {

    @BoundProperty(name = "errorBehaviorThrowException")
    String getValue();

}
