package com.brielmayer.teda.parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeParserTest {

    @Test
    public void testNull() {
        assertEquals("", TypeParser.parse(null));
    }

    @Test
    public void testTime() {
        Time actual = Time.valueOf("22:23:55");
        LocalTime expected = LocalTime.of(22, 23, 55);
        assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testTimeAsString() {
        LocalTime time = LocalTime.of(22, 23, 24);
        assertEquals(time, TypeParser.parse("22:23:24"));
    }

    @Test
    public void testDate() {
        Date actual = Date.valueOf("2016-12-13");
        LocalDate expected = LocalDate.of(2016, 12, 13);
        assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testDateAsString() {
        LocalDate date = LocalDate.of(2016, 12, 13);
        assertEquals(date, TypeParser.parse("2016-12-13"));
    }

    @Test
    public void testDateTime() {
        Timestamp actual = Timestamp.valueOf("2016-12-13 22:23:24");
        LocalDateTime expected = LocalDateTime.of(2016, 12, 13, 22, 23, 24);
        assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testDateTimeAsString() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 13, 22, 23, 24);
        assertEquals(dateTime, TypeParser.parse("2016-12-13T22:23:24"));
    }

    @Test
    public void testBoolean() {
        assertEquals(true, TypeParser.parse(true));
        assertEquals(false, TypeParser.parse(false));
    }

    @Test
    public void testBooleanAsString() {
        assertEquals(true, TypeParser.parse("true"));
        assertEquals(true, TypeParser.parse("TRUE"));
        assertEquals(true, TypeParser.parse("TrUE"));
        assertEquals(false, TypeParser.parse("false"));
        assertEquals(false, TypeParser.parse("FALSE"));
        assertEquals(false, TypeParser.parse("FaLSE"));
    }

    @Test
    public void testLong() {
        assertEquals(new BigInteger("2"), TypeParser.parse(2L));
        assertEquals(new BigInteger("2211313123"), TypeParser.parse(2211313123L));
    }

    @Test
    public void testLongAsString() {
        assertEquals(new BigInteger("0"), TypeParser.parse("0"));
        assertEquals(new BigInteger("1"), TypeParser.parse("1"));
        assertEquals(new BigInteger("-1"), TypeParser.parse("-1"));
        assertEquals(new BigInteger("3424"), TypeParser.parse("3424"));
        assertEquals(new BigInteger("43378373783"), TypeParser.parse("43378373783"));
        assertEquals(new BigInteger("23277283278328328"), TypeParser.parse("23277283278328328"));
        assertEquals(new BigInteger("-23277283278328328"), TypeParser.parse("-23277283278328328"));
    }

    @Test
    public void testByte() {
        assertEquals(new BigInteger("2"), TypeParser.parse((byte) 2));
    }

    @Test
    public void testShort() {
        assertEquals(new BigInteger("2"), TypeParser.parse((short) 2));
    }

    @Test
    public void testInteger() {
        assertEquals(new BigInteger("2"), TypeParser.parse((int) 2));
    }

    @Test
    public void testUuid() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid.toString(), TypeParser.parse(uuid));
    }

    @Test
    public void testString() {
        assertEquals("Hello", TypeParser.parse("Hello"));
    }

}
