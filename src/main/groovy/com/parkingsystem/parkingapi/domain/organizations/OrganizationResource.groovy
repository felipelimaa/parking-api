package com.parkingsystem.parkingapi.domain.organizations

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class OrganizationResource {

    Long id
    OrganizationData organizationData

    static OrganizationResource buildUsing(OrganizationData organizationData) {
        OrganizationResource organizationResource = new OrganizationResource()
        organizationResource.organizationData = organizationData
        organizationResource
    }

}
