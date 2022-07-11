package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationResponse
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import com.parkingsystem.parkingapi.repositories.UtilizationRepository
import com.parkingsystem.parkingapi.resources.UtilizationResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

import java.util.function.Function

@Service
class UtilizationService {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationService)

    @Autowired
    UtilizationRepository utilizationRepository

    @Autowired
    OrganizationRepository organizationRepository

    Mono<UtilizationResponse> doActionsBy(UtilizationResource utilizationResource) {
        logger.createMessage("${this.class.simpleName}.doActionsBy", "Do actions to create Utilization.")
            .with("utilizationResource", utilizationResource)
            .info()

        Mono.just(utilizationResource)
            .flatMap(validateData)
            .flatMap(findOrganization)
            .flatMap(registerUtilization)
            .flatMap(handleSuccess)
    }

    Function<UtilizationResource, Mono<UtilizationResource>> validateData = { UtilizationResource resource ->
        logger.createMessage("${this.class.simpleName}.validateData", "Validate data.")
            .info()

        Mono.just(resource)
    }

    Function<UtilizationResource, Mono<UtilizationResource>> findOrganization = { UtilizationResource resource ->
        logger.createMessage("${this.class.simpleName}.findOrganization", "Searching Organization.")
            .info()

        organizationRepository.findById(resource.utilizationData.organizationId).map{ Organization organization ->
            logger.createMessage("${this.class.simpleName}.findOrganization", "Organization found.")
                .with("organization", organization)
                .info()

                resource.organization = organization
                resource
        }
    }

    Function<UtilizationResource, Mono<UtilizationResource>> registerUtilization = { UtilizationResource resource ->
        logger.createMessage("${this.class.simpleName}.registerUtilization", "Register Utilization.")
            .info()

        utilizationRepository.registerUtilization(
            resource.organization.id,
            resource.utilizationData.plate,
            resource.utilizationData.brand,
            resource.utilizationData.model,
            resource.utilizationData.initialParkingDate,
            UtilizationStatus.PARKED.toString()
        ).map({
            resource.id = it
            resource.utilizationStatus = UtilizationStatus.PARKED
            resource
        })
    }

    Function<UtilizationResource, Mono<UtilizationResponse>> handleSuccess = { UtilizationResource resource ->
        Mono.just(UtilizationResponse.buildUsing(resource))
    }

}