package com.parkingsystem.parkingapi.fixtures

import com.parkingsystem.parkingapi.domain.utilizations.UtilizationEventData
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus

class UtilizationEventDataFixture {

    static UtilizationEventData closed() {
        UtilizationEventData data = new UtilizationEventData()
        data.event = UtilizationStatus.CLOSED.toString()
        data.finishParkingDate = "2022-07-14T15:13:59"
        data
    }

    static UtilizationEventData canceled() {
        UtilizationEventData data = new UtilizationEventData()
        data.event = UtilizationStatus.CANCELED.toString()
        data
    }

    static UtilizationEventData invalidStatus() {
        UtilizationEventData data = new UtilizationEventData()
        data.event = 'AAAAAAAAA'
        data.finishParkingDate = "2022-07-14T15:13:59"
        data
    }

    static UtilizationEventData invalidFinishParkingDate() {
        UtilizationEventData data = new UtilizationEventData()
        data.event = UtilizationStatus.CLOSED.toString()
        data.finishParkingDate = null
        data
    }

}
