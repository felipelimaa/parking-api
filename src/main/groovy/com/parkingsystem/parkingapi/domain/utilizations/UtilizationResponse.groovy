package com.parkingsystem.parkingapi.domain.utilizations

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.resources.UtilizationResource
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationResponse {

    Long id
    Organization organization
    String plate
    String brand
    String model
    String initialParkingDate
    UtilizationStatus utilizationStatus

    static UtilizationResponse buildUsing(UtilizationResource resource) {
        UtilizationResponse response = new UtilizationResponse()
        response.id = resource.id
        response.organization = resource.organization
        response.plate = resource.utilizationData.plate
        response.brand = resource.utilizationData.brand
        response.model = resource.utilizationData.model
        response.initialParkingDate = resource.utilizationData.initialParkingDate
        response.utilizationStatus = resource.utilizationStatus
        response
    }

    static UtilizationResponse buildUsing(Utilization utilization) {
        UtilizationResponse response = new UtilizationResponse()
        response.id = utilization.id
        response.organization = utilization.organization
        response.plate = utilization.plate
        response.brand = utilization.brand
        response.model = utilization.model
        response.initialParkingDate = utilization.initialParkingDate
        response.utilizationStatus = utilization.utilizationStatus
        response

    }

}
