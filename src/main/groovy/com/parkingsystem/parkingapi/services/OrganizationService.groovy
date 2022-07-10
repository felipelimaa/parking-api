package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.organizations.OrganizationResource
import com.parkingsystem.parkingapi.domain.organizations.OrganizationResponse
import com.parkingsystem.parkingapi.infrastructure.exceptions.BadRequestException
import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.OrganizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

import java.util.function.Function

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.FIELD_ORGANIZATION_COST_NOT_DECLARED
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.FIELD_ORGANIZATION_MAXIMUM_CAPACITY_NOT_DECLARED
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.FIELD_ORGANIZATION_NAME_NOT_DECLARED
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ORGANIZATION_NAME_ALREADY_EXISTS

@Service
class OrganizationService {

    private static final Logger logger = LoggerFactory.createLogger(OrganizationService)

    @Autowired
    OrganizationRepository organizationRepository

    Mono<OrganizationResponse> doActionsBy(OrganizationResource organizationResource) {
        logger.createMessage("${this.class.simpleName}.doActionsBy", "Do actions to create Organization.")
            .with("organizationResource", organizationResource)
            .info()

        Mono.just(organizationResource)
            .flatMap(validateData)
            .flatMap(generateId)
            .flatMap(registerOrganization)
    }

    Mono<List<OrganizationResponse>> findAll() {
        logger.createMessage("${this.class.simpleName}.findAll", "Finding all organizations.")
            .info()

        organizationRepository.findAll().map({
            List<OrganizationResponse> result = new ArrayList<>()
            it.forEach{
                OrganizationResponse organizationResponse = new OrganizationResponse()
                organizationResponse.buildWith(it)
                result.add(organizationResponse)
            }
            result
        })
    }

    Mono<OrganizationResponse> findById(String organizationId) {
        logger.createMessage("${this.class.simpleName}.findById", "Finding organization.")
            .with("organizationId", organizationId)
            .info()

        organizationRepository.findById(organizationId).map({
            OrganizationResponse.buildUsing(it)
        })
    }

    Function<OrganizationResource, Mono<OrganizationResource>> validateData = { OrganizationResource resource ->
        logger.createMessage("${this.class.simpleName}.validateData", "Validate data.")
            .info()

        if(!resource.organizationData.name) {
            logger.createMessage("${this.class.simpleName}.validateData", FIELD_ORGANIZATION_NAME_NOT_DECLARED.message)
                .warn()

            return Mono.error(new UnprocessableEntityException(FIELD_ORGANIZATION_NAME_NOT_DECLARED))
        }

        if(!resource.organizationData.cost) {
            logger.createMessage("${this.class.simpleName}.validateData", FIELD_ORGANIZATION_COST_NOT_DECLARED.message)
                    .warn()

            return Mono.error(new UnprocessableEntityException(FIELD_ORGANIZATION_COST_NOT_DECLARED))
        }

        if(!resource.organizationData.maximumCapacity) {
            logger.createMessage("${this.class.simpleName}.validateData", FIELD_ORGANIZATION_MAXIMUM_CAPACITY_NOT_DECLARED.message)
                    .warn()

            return Mono.error(new UnprocessableEntityException(FIELD_ORGANIZATION_MAXIMUM_CAPACITY_NOT_DECLARED))
        }

        if(resource.organizationData.name) {
            return Mono.just(resource)
                .flatMap(validateIfOrganizationNotExistsByName)
        }

        Mono.just(resource)
    }

    Function<OrganizationResource, Mono<OrganizationResource>> validateIfOrganizationNotExistsByName = { OrganizationResource resource ->
        logger.createMessage("${this.class.simpleName}.validateIfOrganizationNotExistsByName", "Verify if organization not exists by name.")
            .info()

        organizationRepository.findOrganizationByName(resource.organizationData.name).map({
            if(it) {
                throwsException(ORGANIZATION_NAME_ALREADY_EXISTS.message)
            }
            resource
        })
    }

    Function<OrganizationResource, Mono<OrganizationResource>> generateId = { OrganizationResource resource ->
        logger.createMessage("${this.class.simpleName}.generateId", "Generating Organization ID.")
            .info()

        organizationRepository.generateOrganizationId().map({
            resource.id = it
            resource
        })
    }

    Function<OrganizationResource, Mono<OrganizationResponse>> registerOrganization = { OrganizationResource resource ->
        logger.createMessage("${this.class.simpleName}.registerOrganization", "Register Organization.")
            .with("organizationResource", resource)
            .info()

        organizationRepository.registerOrganization(resource.organizationData.name, resource.organizationData.cost, resource.organizationData.maximumCapacity).map({
            OrganizationResponse.buildUsing(resource)
        })
    }

    private static void throwsException(String msg) {
        logger.createMessage("${this.class.simpleName}.throwsException", msg).warn()

        throw new BadRequestException(msg)
    }

}
