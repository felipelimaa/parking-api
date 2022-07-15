package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventData
import com.parkingsystem.parkingapi.resources.UtilizationEventResource

class UtilizationEventResourceFixture {

    static UtilizationEventResource valid(Long organizationId, Long utilizationId, UtilizationEventData utilizationEventData) {
        UtilizationEventResource resource = new UtilizationEventResource()
        resource.organizationId = organizationId
        resource.utilizationId = utilizationId
        resource.event = utilizationEventData.event
        resource.finishParkingDate = utilizationEventData.finishParkingDate
        resource
    }

}
