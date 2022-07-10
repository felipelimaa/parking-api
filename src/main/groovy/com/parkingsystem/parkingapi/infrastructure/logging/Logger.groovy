package com.parkingsystem.parkingapi.infrastructure.logging

interface Logger {

    LogMessage createMessage(String component, String message)

}