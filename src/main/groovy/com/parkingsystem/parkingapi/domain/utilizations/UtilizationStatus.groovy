package com.parkingsystem.parkingapi.domain.utilizations

enum UtilizationStatus {

    PARKED,
    CLOSED,
    CANCELED

    static UtilizationStatus findByName(String name){
        return values().find{it.name() == name}
    }

}
