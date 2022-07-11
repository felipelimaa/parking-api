package com.parkingsystem.parkingapi.infrastructure.exceptions

import org.springframework.http.HttpStatus

class BusinessException extends Exception {

    final HttpStatus httpStatus

    BusinessException(String message, HttpStatus httpStatus) {
        super(message as String)
        this.httpStatus = httpStatus
    }
}
