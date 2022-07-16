package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.resources.OrganizationResource
import com.parkingsystem.parkingapi.domain.organizations.OrganizationResponse
import com.parkingsystem.parkingapi.fixtures.OrganizationFixture
import com.parkingsystem.parkingapi.fixtures.OrganizationResourceFixture
import com.parkingsystem.parkingapi.fixtures.OrganizationUpdateResourceFixture
import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import com.parkingsystem.parkingapi.resources.OrganizationUpdateResource
import reactor.core.publisher.Mono
import spock.lang.Specification

class OrganizationServiceSpec extends Specification {

    OrganizationService service
    OrganizationRepository organizationRepository = Mock(OrganizationRepository)

    def setup() {
        service = Spy(OrganizationService)
        service.organizationRepository = organizationRepository
    }

    def 'Should create Organization with success'() {
        given:
        OrganizationResource resource = OrganizationResourceFixture.valid()
        Long organizationId = 1L

        when:
        OrganizationResponse response = Mono.just(resource).flatMap(service.registerOrganization).block()

        then:
        1 * organizationRepository.registerOrganization(
            resource.organizationData.name,
            resource.organizationData.cost,
            resource.organizationData.maximumCapacity
        ) >> Mono.just(
            organizationId
        )

        organizationId == response.id
    }

    def 'Should return UnprocessableEntityException when to try create an Organization without name'() {
        given:
        OrganizationResource resource = OrganizationResourceFixture.valid()
        resource.organizationData.name = null

        when:
        Mono.just(resource).flatMap(service.validateData).block()

        then:
        def e = thrown(Throwable)
        e.cause instanceof UnprocessableEntityException
        e.cause.message.contains(ErrorEnum.FIELD_ORGANIZATION_NAME_NOT_DECLARED.message)
    }

    def 'Should return UnprocessableEntityException when to try create an Organization without cost'() {
        given:
        OrganizationResource resource = OrganizationResourceFixture.valid()
        resource.organizationData.cost = null

        when:
        Mono.just(resource).flatMap(service.validateData).block()

        then:
        def e = thrown(Throwable)
        e.cause instanceof UnprocessableEntityException
        e.cause.message.contains(ErrorEnum.FIELD_ORGANIZATION_COST_NOT_DECLARED.message)
    }

    def 'Should return UnprocessableEntityException when to try create an Organization without maximum_capacity'() {
        given:
        OrganizationResource resource = OrganizationResourceFixture.valid()
        resource.organizationData.maximumCapacity = null

        when:
        Mono.just(resource).flatMap(service.validateData).block()

        then:
        def e = thrown(Throwable)
        e.cause instanceof UnprocessableEntityException
        e.cause.message.contains(ErrorEnum.FIELD_ORGANIZATION_MAXIMUM_CAPACITY_NOT_DECLARED.message)
    }

    def 'Should find an existing organization'() {
        given:
        Long organizationId = 1

        and:
        Organization organization = OrganizationFixture.valid()
        organizationRepository.findById(organizationId) >> Mono.just(organization)

        when:
        OrganizationResponse response = service.findById(organizationId).block()

        then:
        response.id == organization.id
    }

    def 'Should find an not existing organization'() {
        given:
        Long organizationId = 10

        and:
        organizationRepository.findById(organizationId) >> { throw new NotFoundException() }
        service.findById(organizationId) >> { throw new NotFoundException() }

        when:
        service.findById(organizationId).block()

        then:
        thrown(NotFoundException)
    }

    def 'Should update organization with success'() {
        given:
        OrganizationUpdateResource resource = OrganizationUpdateResourceFixture.valid()

        when:
        Mono.just(resource).flatMap(service.updateOrganization).block()

        then:
        1 * organizationRepository.updateOrganization(
            resource.id,
            resource.organizationUpdateData.cost,
            resource.organizationUpdateData.maximumCapacity
        )
    }

}
