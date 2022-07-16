package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventResponse
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.infrastructure.exceptions.InternalServerException
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import com.parkingsystem.parkingapi.repositories.UtilizationEventRepository
import com.parkingsystem.parkingapi.repositories.UtilizationRepository
import com.parkingsystem.parkingapi.resources.UtilizationEventResource
import com.parkingsystem.parkingapi.utils.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

import java.time.Duration
import java.time.LocalDateTime
import java.util.function.Function

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_CALCULATE_COST_UTILIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.FIELD_UTILIZATION_FINISH_PARKING_DATE_NOT_DECLARED
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.UTILIZATION_IS_CLOSED_CANCELED
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.UTILIZATION_STATUS_NOT_FOUND

@Service
class UtilizationEventService {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationEventService)

    @Autowired
    UtilizationEventRepository utilizationEventRepository

    @Autowired
    UtilizationRepository utilizationRepository

    @Autowired
    OrganizationRepository organizationRepository

    Mono<UtilizationEventResponse> doActionsBy(UtilizationEventResource utilizationEventResource) {
        logger.createMessage("${this.class.simpleName}.doActionsBy", "Do actions to update Utilization.")
            .with("utilizationEventResource", utilizationEventResource)
            .info()

        Mono.just(utilizationEventResource)
            .flatMap(findOrganization)
            .flatMap(findUtilization)
            .flatMap(validateData)
            .flatMap(calculateCost)
            .flatMap(updateUtilization)
            .flatMap(findUtilization)
            .flatMap(handleSuccess)
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResource>> validateData = { UtilizationEventResource resource ->
        logger.createMessage("${this.class.simpleName}.validateData", "Validate data.")
            .info()

        if (!UtilizationStatus.findByName(resource.event)) {
            logger.createMessage("${this.class.simpleName}.validateData", UTILIZATION_STATUS_NOT_FOUND.message)
                .warn()

            return Mono.error(new NotFoundException(UTILIZATION_STATUS_NOT_FOUND))
        }

        if([UtilizationStatus.CLOSED, UtilizationStatus.CANCELED].contains(resource.utilization.utilizationStatus)) {
            logger.createMessage("${this.class.simpleName}.validateData", UTILIZATION_IS_CLOSED_CANCELED.message)
                .info()

            return Mono.error(new UnprocessableEntityException(UTILIZATION_IS_CLOSED_CANCELED))
        }

        resource.utilizationStatus = UtilizationStatus.findByName(resource.event)

        if (resource.utilizationStatus == UtilizationStatus.CLOSED && !resource.finishParkingDate) {
            logger.createMessage("${this.class.simpleName}.validateData", FIELD_UTILIZATION_FINISH_PARKING_DATE_NOT_DECLARED.message)
                .warn()

            return Mono.error(new UnprocessableEntityException(FIELD_UTILIZATION_FINISH_PARKING_DATE_NOT_DECLARED))
        }

        Mono.just(resource)
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResource>> findOrganization = { UtilizationEventResource resource ->
        logger.createMessage("${this.class.simpleName}.findOrganization", "Searching Organization.")
            .info()

        organizationRepository.findById(resource.organizationId).map({ Organization organization ->
            logger.createMessage("${this.class.simpleName}.findOrganization", "Organization found.")
                .with("organization", organization)
                .info()

            resource.organization = organization
            resource
        })
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResource>> findUtilization = { UtilizationEventResource resource ->
        logger.createMessage("${this.class.simpleName}.findUtilization", "Searching Utilization.")
            .info()

        utilizationRepository.findById(resource.organizationId, resource.utilizationId).map({ Utilization utilization ->
            logger.createMessage("${this.class.simpleName}.findUtilization", "Utilization found.")
                .with("utilization", utilization)
                .info()

            resource.utilization = utilization
            resource
        })
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResource>> calculateCost = { UtilizationEventResource resource ->
        logger.createMessage("${this.class.simpleName}.calculateCost", "Calculating cost.")
            .info()

        if(resource.utilizationStatus == UtilizationStatus.CLOSED) {

            try {
                def duration = Duration.between(DateUtils.parseDateTime(resource.utilization.initialParkingDate), LocalDateTime.parse(resource.finishParkingDate)).toHours()

                resource.utilization.cost = resource.organization.cost * (duration > 0 ? duration : 1)
                resource.utilization.duration = (Integer) duration
            } catch (Exception e) {
                logger.createMessage("${this.class.simpleName}.calculateCost", ERROR_WHILE_CALCULATE_COST_UTILIZATION.message)
                    .with("utilizationEventResource", resource)
                    .error(e)

                throw new InternalServerException(ERROR_WHILE_CALCULATE_COST_UTILIZATION)
            }

        }

        Mono.just(resource)
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResource>> updateUtilization = { UtilizationEventResource resource ->
        logger.createMessage("${this.class.simpleName}.updateUtilization", "Update utilization.")
            .info()

        utilizationEventRepository.updateUtilization(
            resource.utilizationId,
            resource.finishParkingDate,
            resource.utilization.cost,
            resource.utilizationStatus as String,
            resource.utilization.duration
        )

        Mono.just(resource)
    }

    Function<UtilizationEventResource, Mono<UtilizationEventResponse>> handleSuccess = { UtilizationEventResource resource ->
        Mono.just(UtilizationEventResponse.buildUsing(resource.utilization, resource.organization, resource.utilizationStatus))
    }
}
