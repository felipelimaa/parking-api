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
    ERROR_WHILE_REGISTER_UTILIZATION('Something went wrong while register Utilization'),
    ERROR_WHILE_UPDATE_UTILIZATION('Something went wrong while update Utilization'),
    ERROR_WHILE_FINDING_UTILIZATION('Something went wrong while finding Utilization'),
    ERROR_WHILE_CALCULATE_COST_UTILIZATION('Something went wrong while calculate cost Utilization'),
    FIELD_UTILIZATION_FINISH_PARKING_DATE_NOT_DECLARED("Please inform 'finish_parking_date'"),
    UTILIZATION_NOT_FOUND("Utilization not found!"),
    UTILIZATION_IS_CLOSED_CANCELED("Utilization is closed/canceled"),

    // Utilization Status
    UTILIZATION_STATUS_NOT_FOUND("Utilization status not found!")

    String message

    ErrorEnum(String message) {
        this.message = message
    }

    String toString() {
        message
    }
}
