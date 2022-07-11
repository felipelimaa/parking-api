package com.parkingsystem.parkingapi.infrastructure.exceptions

import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import org.springframework.http.HttpStatus

class NotFoundException extends BusinessException {

    NotFoundException() {
        super(null, HttpStatus.NOT_FOUND)
    }

    NotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND)
    }

    NotFoundException(ErrorEnum errorEnum) {
        super(errorEnum.message, HttpStatus.NOT_FOUND)
    }

}
