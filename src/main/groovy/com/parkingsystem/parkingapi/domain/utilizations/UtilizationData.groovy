package com.parkingsystem.parkingapi.domain.utilizations

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationData {

    Long organizationId
    String plate
    String brand
    String model
    String initialParkingDate
    String finishParkingDate

}
