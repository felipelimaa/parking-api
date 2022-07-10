package com.parkingsystem.parkingapi.configuration

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class FlywayConfiguration {

    @Autowired
    DatasourceProvider datasourceProvider

    @Autowired
    Environment environment

    @Bean("flyway")
    Flyway flyway() {
        if(environment.getProperty("spring.flyway.enabled").toBoolean()) {
            Flyway flyway = new Flyway()
            flyway.setLocations(environment.getProperty("spring.flyway.locations"))
            flyway.dataSource = datasourceProvider.parkingDataSource
            flyway.repair()
            flyway.migrate()
            flyway
        }
    }

}
