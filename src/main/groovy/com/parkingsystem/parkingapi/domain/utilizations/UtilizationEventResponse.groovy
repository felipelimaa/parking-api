package com.parkingsystem.parkingapi.domain.utilizations

import com.parkingsystem.parkingapi.domain.organizations.Organization
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationEventResponse {

    Long id
    Organization organization
    String plate
    String brand
    String model
    String initialParkingDate
    String finishParkingDate
    BigDecimal cost
    UtilizationStatus utilizationStatus
    Integer duration

    static UtilizationEventResponse buildUsing(Utilization utilization, Organization organization, UtilizationStatus utilizationStatus) {
        UtilizationEventResponse response = new UtilizationEventResponse()
        response.id = utilization.id
        response.organization = organization
        response.plate = utilization.plate
        response.brand = utilization.brand
        response.model = utilization.model
        response.initialParkingDate = utilization.initialParkingDate
        response.finishParkingDate = utilization.finishParkingDate
        response.cost = utilization.cost
        response.utilizationStatus = utilization.utilizationStatus
        response.duration = utilization.duration
        response
    }

}
