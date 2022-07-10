package com.parkingsystem.parkingapi.application

import com.parkingsystem.parkingapi.domain.organizations.OrganizationData
import com.parkingsystem.parkingapi.domain.organizations.OrganizationResource
import com.parkingsystem.parkingapi.domain.organizations.OrganizationResponse
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.services.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
class OrganizationController {

    private static final Logger logger = LoggerFactory.createLogger(OrganizationController)

    @Autowired
    OrganizationService organizationService

    @PostMapping("/v1/organizations")
    Mono<OrganizationResponse> doActions(@RequestBody OrganizationData organizationData) {
        logger.createMessage("${this.class.simpleName}.doActions", "Handling POST /v1/organizations")
            .with("organizationData", organizationData)
            .info()
        OrganizationResource organizationResource = OrganizationResource.buildUsing(organizationData)
        organizationService.doActionsBy(organizationResource)
    }

    @GetMapping("/v1/organizations")
    Mono<List<OrganizationResponse>> findAll() {
        logger.createMessage("${this.class.simpleName}.findAll", "Handling GET /v1/organizations")
            .info()
        organizationService.findAll()
    }

}
