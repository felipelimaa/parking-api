package com.parkingsystem.parkingapi.services

import com.parkingsystem.parkingapi.infrastructure.exceptions.UnprocessableEntityException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.resources.UtilizationResource
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono

class UtilizationValidatorService {

    public static final int PLATE_SIZE = 7
    public static final int BRAND_SIZE = 255
    public static final int MODEL_SIZE = 255

    private static final Logger logger = LoggerFactory.createLogger(UtilizationValidatorService.class)

    Mono<UtilizationResource> validate ( UtilizationResource resource ) {
        validateRequiredFields(resource)
        validateSizeFields(resource)

        Mono.just(resource)
    }

    private validateRequiredFields( UtilizationResource resource ) {
        List<String> fields = new ArrayList<String>()

        if(!resource.utilizationData.plate)
            fields.add("plate")

        if (!resource.utilizationData.brand)
            fields.add("brand")

        if (!resource.utilizationData.model)
            fields.add("model")

        if (!fields.isEmpty()) {
            String validationMessage = "The following fields are missing: " + StringUtils.arrayToDelimitedString(fields as Object[], ", ") + "."
            logger.createMessage("${this.class.simpleName}.validateRequiredFields", validationMessage)
                .warn()

            throw new UnprocessableEntityException(validationMessage)
        }

    }

    private validateSizeFields ( UtilizationResource resource ) {
        if ( resource.utilizationData.plate.length() > PLATE_SIZE) {
            String validationMessage = "'plate' size exceeded. Maximum size allowed is: ${PLATE_SIZE}."
            logger.createMessage("${this.class.simpleName}.validateSizeFields", validationMessage)
                .warn()

            throw new UnprocessableEntityException(validationMessage)
        }

        if ( resource.utilizationData.brand.length() > BRAND_SIZE) {
            String validationMessage = "'brand' size exceeded. Maximum size allowed is: ${BRAND_SIZE}."
            logger.createMessage("${this.class.simpleName}.validateSizeFields", validationMessage)
                .warn()

            throw new UnprocessableEntityException(validationMessage)
        }

        if ( resource.utilizationData.model.length() > MODEL_SIZE) {
            String validationMessage = "'model' size exceeded. Maximum size allowed is: ${MODEL_SIZE}."
            logger.createMessage("${this.class.simpleName}.validateSizeFields", validationMessage)
                .warn()

            throw new UnprocessableEntityException(validationMessage)
        }
    }

}
