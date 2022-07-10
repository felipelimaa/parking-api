package com.parkingsystem.parkingapi.repositories.rowmapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

@Component
class OrganizationGenerateIdRowMapper implements RowMapper<String>{

    static final String ORGANIZATION_ID = "Organization_ID"

    @Override
    String mapRow(ResultSet rs, int rowNum) throws SQLException {
        String organization_ID = rs.getString(ORGANIZATION_ID)
        organization_ID
    }

}
