package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.resources.UtilizationResource

class UtilizationResourceFixture {

    static UtilizationResource valid(Organization organization = null) {
        UtilizationResource resource = new UtilizationResource()
        resource.utilizationData = UtilizationDataFixture.valid(organization.id)
        resource.organization = organization
        resource
    }

    static UtilizationResource valid(Long organizationId) {
        UtilizationResource resource = new UtilizationResource()
        resource.utilizationData = UtilizationDataFixture.valid(organizationId)
        resource
    }

    static UtilizationResource valid(Long organizationId, Long utilizationId, String event = null, String finishParkingDate = null) {
        UtilizationResource resource = new UtilizationResource()
        resource.id = utilizationId
        resource.utilizationData = new UtilizationData(organizationId: organizationId, finishParkingDate: finishParkingDate)
        resource.utilizationStatus = event ? UtilizationStatus.findByName(event) : null
        resource
    }

}
