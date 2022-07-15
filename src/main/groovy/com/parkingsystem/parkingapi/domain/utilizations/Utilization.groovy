package com.parkingsystem.parkingapi.domain.utilizations

import com.parkingsystem.parkingapi.domain.organizations.Organization
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class Utilization {

    Long id
    Organization organization
    String plate
    String brand
    String model
    String initialParkingDate
    String finishParkingDate
    BigDecimal cost
    UtilizationStatus utilizationStatus
    String createdAt
    String updatedAt

}
