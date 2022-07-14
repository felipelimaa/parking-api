package com.parkingsystem.parkingapi.domain.utilizations

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationEventData {

    String event
    String finishParkingDate
}
