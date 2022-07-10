package com.parkingsystem.parkingapi.infrastructure.logging

interface LogMessage {

    LogMessage with(String candidateKey, String value)
    LogMessage with(String candidateKey, Object value)
    LogMessage with(String candidateKey, Map<?, ?> value)
    LogMessage with(String candidateKey, Number value)

    void info()
    void info(Throwable t)

    void warn()
    void warn(Throwable t)

    void debug()
    void debug(Throwable t)

    void error()
    void error(Throwable t)

}