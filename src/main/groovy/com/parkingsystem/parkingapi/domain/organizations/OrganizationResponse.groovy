package com.parkingsystem.parkingapi.domain.organizations

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class OrganizationResponse {

    String id
    String name
    BigDecimal cost
    Integer maximumCapacity

    static OrganizationResponse buildUsing(OrganizationResource organizationResource) {
        OrganizationResponse organizationResponse = new OrganizationResponse()
        organizationResponse.id = organizationResource.id
        organizationResponse.name = organizationResource.organizationData.name
        organizationResponse.cost = organizationResource.organizationData.cost
        organizationResponse.maximumCapacity = organizationResource.organizationData.maximumCapacity
        organizationResponse
    }

    def buildWith(Organization organization) {
        this.id = organization.id
        this.name = organization.name
        this.cost = organization.cost
        this.maximumCapacity = organization.maximumCapacity
        return this
    }

}
