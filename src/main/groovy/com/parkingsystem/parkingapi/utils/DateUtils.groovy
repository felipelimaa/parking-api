package com.parkingsystem.parkingapi.utils

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@CompileStatic
@TypeChecked
class DateUtils {

    static final String DATE_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss"
    static final String DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss"

    static String toBrazilGMT(Calendar cal = Calendar.getInstance()) {
        return cal.getTime().toInstant()
            .atZone(ZoneId.of("America/Recife"))
            .format(DateTimeFormatter.ofPattern(DATE_FORMAT))
    }

    static LocalDateTime parseDateTime(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT_DB))
    }

}
