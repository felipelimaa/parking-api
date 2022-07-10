package com.parkingsystem.parkingapi.infrastructure.logging.impl

import com.parkingsystem.parkingapi.infrastructure.logging.LogMessage
import groovy.json.JsonBuilder
import org.apache.logging.log4j.message.MapMessage

class Log4jMessage implements LogMessage {

    private final MapMessage<?, ?> message
    private final org.apache.logging.log4j.Logger logger

    Log4jMessage(org.apache.logging.log4j.Logger logger, MapMessage<?, ?> message){
        this.logger = logger
        this.message = message
    }

    @Override
    Log4jMessage with(String candidateKey, String value) {
        message.with(candidateKey, value)
        return this
    }

    @Override
    Log4jMessage with(String candidateKey, Object value) {
        message.with(candidateKey, new JsonBuilder(value).toString())
        return this
    }

    @Override
    Log4jMessage with(String candidateKey, Map<?, ?> value) {
        message.with(candidateKey, value)
        return this
    }

    @Override
    Log4jMessage with(String candidateKey, Number value) {
        message.with(candidateKey, value.toString())
        return this
    }

    @Override
    void info() {
        this.logger.info(this.message)
    }

    @Override
    void info(Throwable t) {
        this.logger.info(this.message, t)
    }

    @Override
    void warn() {
        this.logger.warn(this.message)
    }

    @Override
    void warn(Throwable t) {
        this.logger.warn(this.message, t)
    }

    @Override
    void debug() {
        this.logger.debug(this.message)
    }

    @Override
    void debug(Throwable t) {
        this.logger.debug(this.message, t)
    }

    @Override
    void error() {
        this.logger.error(this.message)
    }

    @Override
    void error(Throwable t) {
        this.logger.error(this.message, t)
    }
}
