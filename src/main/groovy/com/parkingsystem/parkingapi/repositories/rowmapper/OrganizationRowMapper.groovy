package com.parkingsystem.parkingapi.repositories.rowmapper

import com.parkingsystem.parkingapi.domain.organizations.Organization
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

@Component
class OrganizationRowMapper implements RowMapper<Organization> {

    static final String ORGANIZATION_ID = "Organization_ID"
    static final String NAME = "Name"
    static final String COST = "Cost"
    static final String MAXIMUM_CAPACITY = "MaximumCapacity"

    @Override
    Organization mapRow(ResultSet rs, int i) throws SQLException {
        Organization org = new Organization()
        org.id = rs.getString(ORGANIZATION_ID)
        org.name = rs.getString(NAME)
        org.cost = rs.getBigDecimal(COST)
        org.maximumCapacity = rs.getInt(MAXIMUM_CAPACITY)
        org
    }
}
