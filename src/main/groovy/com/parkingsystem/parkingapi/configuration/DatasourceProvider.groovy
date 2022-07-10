package com.parkingsystem.parkingapi.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

import javax.sql.DataSource

@Component
class DatasourceProvider {

    @Autowired
    @Qualifier("parkingDataSource")
    DataSource parkingDataSource

    DataSource getParkingDataSource() {
        parkingDataSource
    }

}
