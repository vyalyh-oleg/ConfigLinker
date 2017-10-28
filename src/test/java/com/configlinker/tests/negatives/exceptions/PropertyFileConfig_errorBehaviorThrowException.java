package com.configlinker.tests.negatives.exceptions;

import com.configlinker.ErrorBehavior;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;


@BoundObject(sourcePath = "config_errorBehaviorThrowException.properties", errorBehavior = ErrorBehavior.THROW_EXCEPTION, trackPolicy = BoundObject.TrackPolicy.ENABLE)
public interface PropertyFileConfig_errorBehaviorThrowException {

    @BoundProperty(name = "errorBehaviorThrowException")
    String getValue();

}
