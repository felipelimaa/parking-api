package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationResponse
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.fixtures.OrganizationFixture
import com.parkingsystem.parkingapi.fixtures.UtilizationFixture
import com.parkingsystem.parkingapi.fixtures.UtilizationResourceFixture
import com.parkingsystem.parkingapi.infrastructure.ErrorEnum
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import com.parkingsystem.parkingapi.repositories.UtilizationRepository
import com.parkingsystem.parkingapi.resources.UtilizationResource
import reactor.core.publisher.Mono
import spock.lang.Specification

class UtilizationServiceSpec extends Specification {

    UtilizationService service

    UtilizationRepository utilizationRepository = Mock(UtilizationRepository)
    OrganizationRepository organizationRepository = Mock(OrganizationRepository)

    def setup() {
        service = Spy(UtilizationService)
        service.utilizationRepository = utilizationRepository
        service.organizationRepository = organizationRepository
    }

    def 'Should create Utilization with success'() {
        given:
        Organization organization = OrganizationFixture.valid()
        UtilizationResource resource = UtilizationResourceFixture.valid(organization)
        Long utilizationId = 1L

        when:
        UtilizationResource response = Mono.just(resource).flatMap(service.registerUtilization).block()

        then:
        1 * utilizationRepository.registerUtilization(
            resource.utilizationData.organizationId,
            resource.utilizationData.plate,
            resource.utilizationData.brand,
            resource.utilizationData.model,
            resource.utilizationData.initialParkingDate,
            UtilizationStatus.PARKED.toString()
        ) >> Mono.just(utilizationId)

        utilizationId == response.id
    }

    def 'Should find an existing utilization'() {
        given:
        Long organizationId = 1
        Long utilizationId = 1
        UtilizationResource resource = UtilizationResourceFixture.valid(organizationId, utilizationId)

        and:
        Utilization utilization = UtilizationFixture.valid()
        Organization organization = OrganizationFixture.valid()
        resource.organization = organization
        utilizationRepository.findById(organizationId, utilizationId) >> Mono.just(utilization)

        when:
        UtilizationResponse response = Mono.just(resource).flatMap(service.findUtilization).block()

        then:
        response.id == utilization.id

    }

    def 'Should find an not existing utilization'() {
        given:
        Long organizationId = 1
        Long utilizationId = Long.MAX_VALUE
        UtilizationResource resource = UtilizationResourceFixture.valid(organizationId, utilizationId)

        and:
        Organization organization = OrganizationFixture.valid()
        resource.organization = organization
        utilizationRepository.findById(organizationId, utilizationId) >> { throw new NotFoundException() }
        service.findById(resource) >> { throw new NotFoundException() }

        when:
        service.findById(resource).block()

        then:
        thrown(NotFoundException)
    }

    def 'Should not permit a new utilization if car is parked in same organization'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        Utilization utilization2 = UtilizationFixture.valid()
        utilization2.id = 2
        UtilizationResource resource = UtilizationResourceFixture.valid(utilization.organization)

        and:
        utilizationRepository.registerUtilization(
            utilization2.organization.id,
            utilization2.plate,
            utilization2.brand,
            utilization2.model,
            utilization2.initialParkingDate,
            utilization2.utilizationStatus.toString()
        )
        service.doActionsBy(resource) >> { throw new UnprocessableEntityException() }

        when:
        service.doActionsBy(resource).block()

        then:
        thrown(UnprocessableEntityException)

    }

    def 'Should verify if slot available in parking without success'() {
        given:
        Utilization utilization = UtilizationFixture.valid()

        utilization.organization.maximumCapacity = 0
        UtilizationResource resource = UtilizationResourceFixture.valid(utilization.organization)

        and:
        organizationRepository.registerOrganization(resource.organization.name, resource.organization.cost, resource.organization.maximumCapacity)

        when:
        Mono.just(resource).flatMap(service.verifySpaceInParking).block()

        then:
        1 * organizationRepository.verifyIfSlotInParking(resource.organization.id) >> Mono.just(false)
        def e = thrown(Throwable)
        e.cause instanceof UnprocessableEntityException
        e.cause.message.contains(ErrorEnum.NOT_EXISTS_SLOT_EMPTY_IN_ORGANIZATION.message)
    }

    def 'Should verify if slot available in parking with success'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        UtilizationResource resource = UtilizationResourceFixture.valid(utilization.organization)

        and:
        organizationRepository.registerOrganization(resource.organization.name, resource.organization.cost, resource.organization.maximumCapacity)

        when:
        def response = Mono.just(resource).flatMap(service.verifySpaceInParking).block()

        then:
        1 * organizationRepository.verifyIfSlotInParking(resource.organization.id) >> Mono.just(true)
        resource == response
    }

}
