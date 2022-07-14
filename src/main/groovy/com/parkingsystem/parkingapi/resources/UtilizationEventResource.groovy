package com.parkingsystem.parkingapi.resources

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationEventResource {

    Long utilizationId
    Long organizationId
    String event
    String finishParkingDate

    Organization organization
    Utilization utilization
    UtilizationStatus utilizationStatus

    static UtilizationEventResource buildUsing(Long organizationId, Long utilizationId, UtilizationEventData utilizationEventData) {
        UtilizationEventResource resource = new UtilizationEventResource()
        resource.organizationId = organizationId
        resource.utilizationId = utilizationId
        resource.event = utilizationEventData.event
        resource.finishParkingDate = utilizationEventData.finishParkingDate ? utilizationEventData.finishParkingDate : null
        resource
    }

}
