package com.parkingsystem.parkingapi.repositories.rowmapper

import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet

@Component
class UtilizationRowMapper implements RowMapper<Utilization> {

    static final String UTILIZATION_ID = "Utilization_ID"
    static final String ORGANIZATION__ID = "Organization__ID"
    static final String ORGANIZATION_NAME = "OrganizationName"
    static final String ORGANIZATION_COST = "OrganizationCost"
    static final String ORGANIZATION_MAXIMUM_CAPACITY = "OrganizationMaximumCapacity"
    static final String PLATE = "Plate"
    static final String BRAND = "Brand"
    static final String MODEL = "Model"
    static final String INITIAL_PARKING_DATE = "InitialParkingDate"
    static final String FINISH_PARKING_DATE = "FinishParkingDate"
    static final String COST = "Cost"
    static final String UTILIZATION_STATUS = "UtilizationStatus"
    static final String CREATED_AT = "CreatedAt"
    static final String UPDATED_AT = "UpdatedAt"

    @Override
    Utilization mapRow(ResultSet rs, int rowNum) {
        Utilization utilization = new Utilization()
        utilization.id = rs.getLong(UTILIZATION_ID)
        utilization.organization = new Organization(
            id: rs.getLong(ORGANIZATION__ID),
            name: rs.getString(ORGANIZATION_NAME),
            cost: rs.getBigDecimal(ORGANIZATION_COST),
            maximumCapacity: rs.getInt(ORGANIZATION_MAXIMUM_CAPACITY)
        )
        utilization.plate = rs.getString(PLATE)
        utilization.brand = rs.getString(BRAND)
        utilization.model = rs.getString(MODEL)
        utilization.initialParkingDate = rs.getString(INITIAL_PARKING_DATE)
        utilization.finishParkingDate = rs.getString(FINISH_PARKING_DATE)
        utilization.cost = rs.getBigDecimal(COST)
        utilization.utilizationStatus = rs.getString(UTILIZATION_STATUS) ? UtilizationStatus.valueOf(rs.getString(UTILIZATION_STATUS)) : null
        utilization.createdAt = rs.getString(CREATED_AT)
        utilization.updatedAt = rs.getString(UPDATED_AT)
        utilization
    }

}
