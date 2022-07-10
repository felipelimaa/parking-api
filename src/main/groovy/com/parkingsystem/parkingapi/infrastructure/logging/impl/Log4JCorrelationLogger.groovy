package com.parkingsystem.parkingapi.infrastructure.logging.impl

import com.parkingsystem.parkingapi.infrastructure.logging.LogMessage
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.telemetry.Log4JOpenTelemetryMapMessage
import com.parkingsystem.parkingapi.utils.EnvironmentUtils
import org.apache.logging.log4j.LogManager
import org.springframework.util.StringUtils

class Log4JCorrelationLogger implements Logger {

    private static final String COMPONENT = "Component"

    private final org.apache.logging.log4j.Logger logger
    private final EnvironmentUtils environmentUtils

    Log4JCorrelationLogger(Class<?> referencedClass, EnvironmentUtils environmentUtils) {
        this.environmentUtils = environmentUtils
        this.logger = LogManager.getLogger(referencedClass)
    }

    @Override
    LogMessage createMessage(String component, String message) {
        return new Log4jMessage(logger, createBaseLog4JOpenTelemtryMessage(component, message))
    }

    private Log4JOpenTelemetryMapMessage createBaseLog4JOpenTelemtryMessage(String component, String message) {
        Log4JOpenTelemetryMapMessage log4jMessage = new Log4JOpenTelemetryMapMessage(message)
            .with(COMPONENT, safeGetString(component))

        return log4jMessage
    }

    private String safeGetString(String str) {
        return StringUtils.hasText(str) ? str : ""
    }
}
