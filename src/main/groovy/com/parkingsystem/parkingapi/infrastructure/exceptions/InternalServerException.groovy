package com.parkingsystem.parkingapi.infrastructure.exceptions

import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import org.springframework.http.HttpStatus

class InternalServerException extends BusinessException {

    InternalServerException(String msg) {
        super(msg, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    InternalServerException(ErrorEnum errorEnum) {
        super(errorEnum.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
