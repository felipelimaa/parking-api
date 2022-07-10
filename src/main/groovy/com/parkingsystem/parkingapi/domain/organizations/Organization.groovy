package com.parkingsystem.parkingapi.domain.organizations

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, ignoreNulls = true, includePackage = false)
class Organization {

    String id
    String name
    BigDecimal cost
    Integer maximumCapacity

}
