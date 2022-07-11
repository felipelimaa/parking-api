package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.organizations.Organization

class OrganizationFixture {

    static Organization valid() {
        Organization organization = new Organization()
        organization.id = 1
        organization.name = 'Test organization'
        organization.cost = 3
        organization.maximumCapacity = 10
        organization
    }

}
