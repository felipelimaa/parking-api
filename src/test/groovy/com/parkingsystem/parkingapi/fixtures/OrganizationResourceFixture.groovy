package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.organizations.OrganizationResource

class OrganizationResourceFixture {

    static OrganizationResource valid() {
        OrganizationResource resource = new OrganizationResource()
        resource.organizationData = OrganizationDataFixture.valid()
        resource
    }
}
