package com.parkingsystem.parkingapi.infrastructure.logging

import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.impl.Log4JCorrelationLogger
import com.parkingsystem.parkingapi.utils.EnvironmentUtils

class LoggerFactory {

    private static final EnvironmentUtils environmentUtils

    static {
        environmentUtils = new EnvironmentUtils()
    }

    static Logger createLogger(Class<?> toLog) {
        return new Log4JCorrelationLogger(toLog, environmentUtils)
    }
}
