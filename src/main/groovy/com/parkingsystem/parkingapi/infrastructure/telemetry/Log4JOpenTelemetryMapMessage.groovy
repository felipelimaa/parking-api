package com.parkingsystem.parkingapi.infrastructure.telemetry

import org.apache.logging.log4j.message.MapMessage
import org.apache.logging.log4j.util.EnglishEnums

class Log4JOpenTelemetryMapMessage extends MapMessage<Log4JOpenTelemetryMapMessage, String>{

    private String message

    enum Format {
        XML, FULL
    }

    Log4JOpenTelemetryMapMessage(String message) {
        this.message = message
    }

    @Override
    String[] getFormats() {
        final String[] formats = new String[Format.values().length]
        int i = 0
        for(final Format format : Format.values()) {
            formats[i++] = format.name()
        }
        return formats
    }

    @Override
    void formatTo(final StringBuilder buffer) {
        asString(Format.FULL, buffer)
    }

    @Override
    void formatTo(final String[] formats, final StringBuilder buffer) {
        asString(getSelectedFormat(formats), buffer)
    }

    @Override
    String getFormat() {
        return this.message
    }

    private Format getSelectedFormat(final String[] formats) {
        if(formats != null && formats.length > 0) {
            for(int i = 0; i < formats.length; i++) {
                final String format = formats[i]
                if(Format.XML.name().equalsIgnoreCase(format)) {
                    return Format.XML
                } elseif(Format.FULL.name().equalsIgnoreCase(format)) {
                    return Format.FULL
                }
            }
            return null
        }
        return Format.FULL
    }

    protected void setMessageFormat(final String msg) {
        this.message = msg
    }

    @Override
    String asString() {
        return asString(Format.FULL, new StringBuilder())
    }

    @Override
    String asString(final String format) {
        try {
            return asString(EnglishEnums.valueOf(Format.class, format), new StringBuilder())
        } catch(final IllegalArgumentException ex) {
            return asString()
        }
    }

    final String asString(final Format format) {
        final StringBuilder sb = new StringBuilder()
        asString(format, sb)
        return sb.toString()
    }

    final String asString(final Format format, StringBuilder sb) {
        final String msg = getFormat()
        if(msg != null) {
            sb.append(msg)
        }
        return sb.toString()
    }

    @Override
    String getFormattedMessage(){
        return asString(Format.FULL, new StringBuilder())
    }

    @Override
    String getFormattedMessage(final String[] formats) {
        return asString(getSelectedFormat(formats), new StringBuilder())
    }

    @Override
    String toString() {
        return asString(null, new StringBuilder())
    }


}
