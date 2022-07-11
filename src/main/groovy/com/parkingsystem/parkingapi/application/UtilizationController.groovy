package com.parkingsystem.parkingapi.application

import com.parkingsystem.parkingapi.domain.utilizations.UtilizationData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationResponse
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.resources.UtilizationResource
import com.parkingsystem.parkingapi.services.UtilizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
class UtilizationController {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationController)

    @Autowired
    UtilizationService utilizationService

    @PostMapping("/v1/utilizations")
    Mono<UtilizationResponse> doActions(@RequestBody UtilizationData utilizationData) {
        logger.createMessage("${this.class.simpleName}.doActions", "Handling POST /v1/utilizations")
            .with("utilizationData", utilizationData)
            .info()
        UtilizationResource utilizationResource = UtilizationResource.buildUsing(utilizationData)
        utilizationService.doActionsBy(utilizationResource)
    }

}
