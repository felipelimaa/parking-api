package com.parkingsystem.parkingapi.application

import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventResponse
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.resources.UtilizationEventResource
import com.parkingsystem.parkingapi.services.UtilizationEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
class UtilizationEventController {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationEventController)

    @Autowired
    UtilizationEventService utilizationEventService

    @PostMapping("/v1/utilizations/{utilizationId}/event")
    Mono<UtilizationEventResponse> event(@RequestHeader Long organizationId,
                                         @PathVariable Long utilizationId,
                                         @RequestBody UtilizationEventData utilizationEventData
    ) {
        logger.createMessage("${this.class.simpleName}.event", "Handling POST /v1/utilizations/${utilizationId}/event")
                .with("organizationId", organizationId)
                .with("utilizationId", utilizationId)
                .info()

        UtilizationEventResource resource = UtilizationEventResource.buildUsing(organizationId, utilizationId, utilizationEventData)

        utilizationEventService.doActionsBy(resource)
    }

}
