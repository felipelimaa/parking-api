package com.parkingsystem.parkingapi.infrastructure

import com.parkingsystem.parkingapi.infrastructure.exceptions.BadRequestException
import com.parkingsystem.parkingapi.infrastructure.exceptions.InternalServerException
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerException)
    ResponseEntity handleInternalServerException(InternalServerException internalServerException) {
        ResponseEntity.status(internalServerException.httpStatus)
            .body(new ErrorResponse(message: internalServerException.message))
    }

    @ExceptionHandler(BadRequestException)
    ResponseEntity handleBadRequestException(BadRequestException badRequestException) {
        ResponseEntity.status(badRequestException.httpStatus)
            .body(new ErrorResponse(message: badRequestException.message))
    }

    @ExceptionHandler(UnprocessableEntityException)
    ResponseEntity handleUnprocessableEntityException(UnprocessableEntityException unprocessableEntityException) {
        ResponseEntity.status(unprocessableEntityException.httpStatus)
            .body(new ErrorResponse(message: unprocessableEntityException.message))
    }

    @ExceptionHandler(NotFoundException)
    ResponseEntity handleNotFoundException(NotFoundException notFoundException) {
        ResponseEntity.status(notFoundException.httpStatus)
            .body(new ErrorResponse(message: notFoundException.message))
    }

}
