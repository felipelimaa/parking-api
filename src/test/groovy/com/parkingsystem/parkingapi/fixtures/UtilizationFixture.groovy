package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus

class UtilizationFixture {

    static Utilization valid() {
        Utilization utilization = new Utilization()
        utilization.id = 1
        utilization.organization = OrganizationFixture.valid()
        utilization.plate = "AAA0000"
        utilization.brand = "Chevrolet"
        utilization.model = "Prisma"
        utilization.initialParkingDate = "2022-07-14 14:12:59"
        utilization.utilizationStatus = UtilizationStatus.PARKED
        utilization
    }

    static Utilization closed() {
        Utilization utilization = new Utilization()
        utilization.id = 1
        utilization.organization = OrganizationFixture.valid()
        utilization.plate = "AAA0000"
        utilization.brand = "Chevrolet"
        utilization.model = "Prisma"
        utilization.initialParkingDate = "2022-07-14 14:12:59"
        utilization.finishParkingDate  = "2022-07-14 15:13:59"
        utilization.cost = 3
        utilization.utilizationStatus = UtilizationStatus.CLOSED
        utilization
    }
}
