package com.parkingsystem.parkingapi.infrastructure.exceptions

import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import org.springframework.http.HttpStatus

class NotFoundException extends BusinessException {

    NotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND)
    }

    NotFoundException(ErrorEnum errorEnum) {
        super(errorEnum.message, HttpStatus.NOT_FOUND)
    }

}
