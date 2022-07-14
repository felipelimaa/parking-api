package com.parkingsystem.parkingapi.resources

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class UtilizationResource {

    Long id
    UtilizationData utilizationData
    Organization organization
    UtilizationStatus utilizationStatus
    Utilization utilization

    static UtilizationResource buildUsing(UtilizationData utilizationData) {
        UtilizationResource utilizationResource = new UtilizationResource()
        utilizationResource.utilizationData = utilizationData
        utilizationResource
    }

    static UtilizationResource buildUsing(Utilization utilization) {
        UtilizationResource utilizationResource = new UtilizationResource()
        utilizationResource.utilization = utilization
        utilizationResource
    }

    static UtilizationResource buildUsing(Long organizationId, Long utilizationId, String event = null, String finishParkingDate = null) {
        UtilizationResource utilizationResource = new UtilizationResource()
        utilizationResource.id = utilizationId
        utilizationResource.utilizationData = new UtilizationData(organizationId: organizationId, finishParkingDate: finishParkingDate)
        utilizationResource.utilizationStatus = event ? UtilizationStatus.valueOf(event) : null
        utilizationResource
    }
}
