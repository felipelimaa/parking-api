package com.parkingsystem.parkingapi.repositories.rowmapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

@Component
class OrganizationCountRowMapper implements RowMapper<Boolean> {

    static final String ORGANIZATION_NAME_EXISTS = "organizationNameExists"

    @Override
    Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
        Boolean organizationNameExists = rs.getBoolean(ORGANIZATION_NAME_EXISTS)
        organizationNameExists
    }

}
