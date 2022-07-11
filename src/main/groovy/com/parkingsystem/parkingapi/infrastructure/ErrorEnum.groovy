package com.parkingsystem.parkingapi.infrastructure

enum ErrorEnum {

    // Organizations
    ERROR_WHILE_REGISTER_ORGANIZATION('Something went wrong while register Organization'),
    ERROR_WHILE_FINDING_ORGANIZATION('Something went wrong while finding Organization'),
    ERROR_WHILE_GENERATE_ORGANIZATION_ID('Somethin went wrong while generate Organization_ID'),
    FIELD_ORGANIZATION_NAME_NOT_DECLARED("Please inform 'name'"),
    FIELD_ORGANIZATION_COST_NOT_DECLARED("Please inform 'cost'"),
    FIELD_ORGANIZATION_MAXIMUM_CAPACITY_NOT_DECLARED("Please inform 'maximum_capacity'"),
    ORGANIZATION_NAME_ALREADY_EXISTS("Organization name already exists."),
    ORGANIZATION_NOT_FOUND("Organization not found!"),

    // Utilizations
    ERROR_WHILE_REGISTER_UTILIZATION('Something went wrong while register Utilization')

    String message

    ErrorEnum(String message) {
        this.message = message
    }

    String toString() {
        message
    }
}
