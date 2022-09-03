package com.parkingsystem.parkingapi.repositories.rowmapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

@Component
class OrganizationSpaceEmptyRowMapper implements RowMapper<Boolean>{

    static final String CAPACITY_AVAILABLE = "capacityAvailable"

    @Override
    Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
        Boolean capacityAvailable = rs.getBoolean(CAPACITY_AVAILABLE)
        capacityAvailable
    }

}
