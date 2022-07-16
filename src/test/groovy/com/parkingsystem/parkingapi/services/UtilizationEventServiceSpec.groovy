package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventResponse
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.fixtures.OrganizationFixture
import com.parkingsystem.parkingapi.fixtures.UtilizationEventDataFixture
import com.parkingsystem.parkingapi.fixtures.UtilizationEventResourceFixture
import com.parkingsystem.parkingapi.fixtures.UtilizationFixture
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import com.parkingsystem.parkingapi.repositories.UtilizationEventRepository
import com.parkingsystem.parkingapi.repositories.UtilizationRepository
import com.parkingsystem.parkingapi.resources.UtilizationEventResource
import reactor.core.publisher.Mono
import spock.lang.Specification

class UtilizationEventServiceSpec extends Specification {

    UtilizationEventService service

    UtilizationEventRepository utilizationEventRepository = Mock(UtilizationEventRepository)
    OrganizationRepository organizationRepository = Mock(OrganizationRepository)
    UtilizationRepository utilizationRepository = Mock(UtilizationRepository)

    def setup() {
        service = Spy(UtilizationEventService)
        service.utilizationEventRepository = utilizationEventRepository
        service.utilizationRepository = utilizationRepository
        service.organizationRepository = organizationRepository
    }

    def 'Should update utilization with event canceled'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        UtilizationEventData utilizationEventData = UtilizationEventDataFixture.canceled()
        UtilizationEventResource resource = UtilizationEventResourceFixture.valid(utilization.organization.id, utilization.id, utilizationEventData)
        resource.utilization = utilization
        resource.organization = utilization.organization
        resource.utilizationStatus = UtilizationStatus.findByName(resource.event)

        when:
        Mono.just(resource).flatMap(service.updateUtilization).block()

        then:
        1 * utilizationEventRepository.updateUtilization(
            resource.utilization.id,
            resource.finishParkingDate,
            resource.utilization.cost,
            resource.utilizationStatus.toString(),
            resource.utilization.duration
        )
    }

    def 'Should update utilization with event closed'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        UtilizationEventData utilizationEventData = UtilizationEventDataFixture.closed()
        UtilizationEventResource resource = UtilizationEventResourceFixture.valid(utilization.organization.id, utilization.id, utilizationEventData)
        resource.utilization = utilization
        resource.organization = utilization.organization
        resource.utilizationStatus = UtilizationStatus.findByName(resource.event)

        and:
        Mono.just(resource).flatMap(service.calculateCost).block()

        when:
        Mono.just(resource).flatMap(service.updateUtilization).block()

        then:
        1 * utilizationEventRepository.updateUtilization(
            resource.utilization.id,
            resource.finishParkingDate,
            resource.utilization.cost,
            resource.utilizationStatus.toString(),
            resource.utilization.duration
        )
    }

    def 'Should update utilization already status is closed'() {
        given:
        Utilization utilization = UtilizationFixture.closed()
        UtilizationEventData utilizationEventData = UtilizationEventDataFixture.closed()
        UtilizationEventResource resource = UtilizationEventResourceFixture.valid(utilization.organization.id, utilization.id, utilizationEventData)
        resource.utilization = utilization
        resource.organization = utilization.organization

        and:
        service.doActionsBy(resource) >> { throw new UnprocessableEntityException() }

        when:
        service.doActionsBy(resource).block()

        then:
        thrown(UnprocessableEntityException)
    }

    def 'Should update utilization with an invalid event'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        UtilizationEventData data = UtilizationEventDataFixture.invalidStatus()
        UtilizationEventResource resource = UtilizationEventResourceFixture.valid(utilization.organization.id, utilization.id, data)
        resource.utilization = utilization
        resource.organization = utilization.organization

        and:
        service.doActionsBy(resource) >> { throw new NotFoundException() }

        when:
        service.doActionsBy(resource).block()

        then:
        thrown(NotFoundException)
    }

    def 'Should try to update utilization without finishParkingDate'() {
        given:
        Utilization utilization = UtilizationFixture.valid()
        UtilizationEventData data = UtilizationEventDataFixture.invalidFinishParkingDate()
        UtilizationEventResource resource = UtilizationEventResourceFixture.valid(utilization.organization.id, utilization.id, data)
        resource.utilization = utilization
        resource.organization = utilization.organization

        and:
        service.doActionsBy(resource) >> { throw new UnprocessableEntityException() }

        when:
        service.doActionsBy(resource).block()

        then:
        thrown(UnprocessableEntityException)
    }

}
