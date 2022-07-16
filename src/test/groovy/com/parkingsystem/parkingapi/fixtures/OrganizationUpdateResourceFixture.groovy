package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.resources.OrganizationUpdateResource

class OrganizationUpdateResourceFixture {

    static OrganizationUpdateResource valid() {
        OrganizationUpdateResource resource = new OrganizationUpdateResource()
        resource.id = 1
        resource.organizationUpdateData = OrganizationUpdateDataFixture.valid()
        resource
    }

}
