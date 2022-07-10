package com.parkingsystem.parkingapi.infrastructure

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class ErrorResponse {

    String key
    String message

}
