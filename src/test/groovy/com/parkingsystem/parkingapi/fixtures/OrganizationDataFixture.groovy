package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.organizations.OrganizationData

class OrganizationDataFixture {

    static OrganizationData valid() {
        OrganizationData organizationData = new OrganizationData()
        organizationData.name = 'Test Organization'
        organizationData.cost = 3
        organizationData.maximumCapacity = 10
        organizationData
    }
}
