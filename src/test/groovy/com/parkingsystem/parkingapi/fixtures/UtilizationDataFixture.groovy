package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.utilizations.UtilizationData

class UtilizationDataFixture {

    static UtilizationData valid(Long organizationId = null) {
        UtilizationData utilizationData = new UtilizationData()
        utilizationData.organizationId = organizationId
        utilizationData.plate = "AAA0000"
        utilizationData.brand = "Chevrolet"
        utilizationData.model = "Prisma"
        utilizationData.initialParkingDate = "2022-07-14T14:12:59"
        utilizationData
    }

}
