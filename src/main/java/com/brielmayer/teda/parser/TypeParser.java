package com.brielmayer.teda.parser;

import com.brielmayer.teda.exception.TedaException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_TIME;

public class TypeParser {

    private static final Pattern BOOL_PATTERN = Pattern.compile("^(true|false)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LONG_PATTERN = Pattern.compile("^-?(0|[1-9][0-9]*)$");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^-?(0|[1-9][0-9]*).[0-9]+$");

    // ----------------------------------------------------
    // java.sql.Date        -> java.time.LocalDate
    // java.sql.Timestamp   -> java.time.LocalDateTime
    // java.sql.Time        -> java.time.LocalTime
    // ----------------------------------------------------
    // java.lang.Boolean    -> java.lang.Boolean
    // ----------------------------------------------------
    // java.lang.Byte       -> java.lang.Long
    // java.lang.Short      -> java.lang.Long
    // java.lang.Integer    -> java.lang.Long
    // java.lang.Long       -> java.lang.Long
    // ----------------------------------------------------
    // java.lang.Float      -> java.lang.Double
    // java.lang.Double     -> java.lang.Double
    // ----------------------------------------------------
    // java.util.UUID       -> java.lang.String
    // ----------------------------------------------------
    public static Object parse(Object value) {

        // default value is an empty String
        if (value == null) {
            return "";
        }

        if(value instanceof Boolean) {
            return value;
        }

        // java.sql.Date
        if(value instanceof Date) {
            Date date = (Date) value;
            return date.toLocalDate();
        }

        // java.sql.Timestamp
        if(value instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) value;
            return timestamp.toLocalDateTime();
        }

        // java.sql.Time
        if(value instanceof Time) {
            Time time = (Time) value;
            return time.toLocalTime();
        }

        // java.lang.Byte
        if(value instanceof Byte) {
            return Long.valueOf((Byte) value);
        }

        // java.lang.Short
        if(value instanceof Short) {
            return Long.valueOf((Short) value);
        }

        // java.lang.Integer
        if(value instanceof Integer) {
            return Long.valueOf((Integer) value);
        }

        // java.lang.Long
        if(value instanceof Long) {
            return (Long) value;
        }

        // java.lang.Float
        if(value instanceof Float) {
            return Double.valueOf((Float)value);
        }

        // java.lang.Double
        if(value instanceof Double) {
            return (Double) value;
        }

        // java.util.UUID
        if(value instanceof UUID) {
            UUID uuid = (UUID) value;
            return uuid.toString();
        }

        // from this point only try to parse strings
        if(!(value instanceof String)) {
            throw new TedaException("Type %s not supported", value.getClass().getSimpleName());
        }

        // java.lang.Boolean
        if (BOOL_PATTERN.matcher((String) value).matches()) {
            return Boolean.parseBoolean((String) value);
        }

        // java.lang.Long
        if (LONG_PATTERN.matcher((String) value).matches()) {
            return Long.valueOf((String) value);
        }

        // java.lang.double
        if(DOUBLE_PATTERN.matcher((String) value).matches()) {
            return Double.valueOf((String) value);
        }

        // java.time.LocalDateTime
        // ISO Date without offset: '2011-12-03'
        try {
            return LocalDate.parse((String) value);
        } catch(DateTimeException e) {};

        // java.time.LocalTime
        // Time without offset: '10:15:30'
        try {
            return LocalTime.parse((String) value);
        } catch(DateTimeException e) {};

        // java.time.LocalDateTime
        //  ISO Local Date and Time: '2011-12-03T10:15:30'
        try {
            return LocalDateTime.parse((String) value);
        } catch(DateTimeException e) {};

        return value;

    }

}
