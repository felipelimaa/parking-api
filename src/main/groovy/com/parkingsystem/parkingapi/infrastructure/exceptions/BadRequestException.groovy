package com.parkingsystem.parkingapi.infrastructure.exceptions

import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import org.springframework.http.HttpStatus

class BadRequestException extends BusinessException {

    BadRequestException(String msg) {
        super(msg, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    BadRequestException(ErrorEnum errorEnum) {
        super(errorEnum.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
