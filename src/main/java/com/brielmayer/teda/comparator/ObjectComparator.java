package com.brielmayer.teda.comparator;

import com.brielmayer.teda.exception.TedaException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ObjectComparator {

    private static ObjectComparator instance;

    private ObjectComparator() {
    }

    public static ObjectComparator getInstance() {
        if (instance == null) {
            instance = new ObjectComparator();
        }
        return instance;
    }

    public boolean compare(Object o1, Object o2) {

        if (!o1.getClass().getSimpleName().equals(o2.getClass().getSimpleName())) {
            throw new TedaException("Types are not equal %s and %s with o1 value %s", o1.getClass().getSimpleName(), o2.getClass().getSimpleName(), o1.toString());
        }

        if (o1 instanceof String) {
            return ((String) o1).equals((String) o2);
        }

        if (o1 instanceof Boolean) {
            return ((boolean) o1) == ((boolean) o2);
        }

        // java.sql.Date
        if (o1 instanceof LocalDate) {
            return ((LocalDate) o1).compareTo((LocalDate) o2) == 0;
        }

        // java.sql.Timestamp
        if (o1 instanceof LocalDateTime) {
            return ((LocalDateTime) o1).compareTo((LocalDateTime) o2) == 0;
        }

        // java.sql.Time
        if (o1 instanceof LocalTime) {
            return ((LocalTime) o1).compareTo((LocalTime) o2) == 0;
        }

        // java.lang.Long
        if (o1 instanceof Long) {
            return ((long) o1) == ((long) o2);
        }

        // java.lang.Float
        if (o1 instanceof Float) {
            return ((float) o1) == ((float) o2);
        }

        // java.lang.Double
        if (o1 instanceof Double) {
            return ((double) o1) == ((double) o2);
        }

        throw new TedaException("Type %s not allowed", o1.getClass().getSimpleName());
    }
}
