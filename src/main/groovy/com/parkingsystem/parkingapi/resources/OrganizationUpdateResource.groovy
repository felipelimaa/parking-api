package com.parkingsystem.parkingapi.resources

import com.parkingsystem.parkingapi.domain.organizations.Organization

import com.parkingsystem.parkingapi.domain.organizations.OrganizationUpdateData
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class OrganizationUpdateResource {

    Long id
    OrganizationUpdateData organizationUpdateData

    Organization organization

    static OrganizationUpdateResource buildUsing(Long organizationId, OrganizationUpdateData organizationUpdateData) {
        OrganizationUpdateResource resource = new OrganizationUpdateResource()
        resource.id = organizationId
        resource.organizationUpdateData = organizationUpdateData
        resource
    }

}
