package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.organizations.OrganizationUpdateData

class OrganizationUpdateDataFixture {

    static OrganizationUpdateData valid() {
        OrganizationUpdateData data = new OrganizationUpdateData()
        data.cost = 20
        data.maximumCapacity = 10
        data
    }

}
