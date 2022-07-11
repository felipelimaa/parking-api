package com.parkingsystem.parkingapi.infrastructure.exceptions

import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import org.springframework.http.HttpStatus

class UnprocessableEntityException extends BusinessException {

    UnprocessableEntityException(String msg) {
        super(msg, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    UnprocessableEntityException(ErrorEnum errorEnum) {
        super(errorEnum.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}
