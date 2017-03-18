package com.brielmayer.teda.parser;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class TypeParserTest {

    @Test
    public void testNull() {
        Assert.assertEquals("", TypeParser.parse(null));
    }

    @Test
    public void testTime() {
        Time actual = Time.valueOf("22:23:55");
        LocalTime expected = LocalTime.of(22, 23, 55);
        Assert.assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testTimeAsString() {
        LocalTime time = LocalTime.of(22, 23, 24);
        Assert.assertEquals(time, TypeParser.parse("22:23:24"));
    }

    @Test
    public void testDate() {
        Date actual = Date.valueOf("2016-12-13");
        LocalDate expected = LocalDate.of(2016, 12, 13);
        Assert.assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testDateAsString() {
        LocalDate date = LocalDate.of(2016, 12, 13);
        Assert.assertEquals(date, TypeParser.parse("2016-12-13"));
    }

    @Test
    public void testDateTime() {
        Timestamp actual = Timestamp.valueOf("2016-12-13 22:23:24");
        LocalDateTime expected = LocalDateTime.of(2016, 12, 13, 22, 23, 24);
        Assert.assertEquals(expected, TypeParser.parse(actual));
    }

    @Test
    public void testDateTimeAsString() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 13, 22, 23, 24);
        Assert.assertEquals(dateTime, TypeParser.parse("2016-12-13T22:23:24"));
    }
    @Test
    public void testBoolean() {
        Assert.assertEquals(true, TypeParser.parse(true));
        Assert.assertEquals(false, TypeParser.parse(false));
    }

    @Test
    public void testBooleanAsString() {
        Assert.assertEquals(true, TypeParser.parse("true"));
        Assert.assertEquals(true, TypeParser.parse("TRUE"));
        Assert.assertEquals(true, TypeParser.parse("TrUE"));
        Assert.assertEquals(false, TypeParser.parse("false"));
        Assert.assertEquals(false, TypeParser.parse("FALSE"));
        Assert.assertEquals(false, TypeParser.parse("FaLSE"));
    }

    @Test
    public void testLong() {
        Assert.assertEquals(2L, TypeParser.parse(2L));
        Assert.assertEquals(2211313123L, TypeParser.parse(2211313123L));
    }

    @Test
    public void testLongAsString() {
        Assert.assertEquals(0L, TypeParser.parse("0"));
        Assert.assertEquals(1L, TypeParser.parse("1"));
        Assert.assertEquals(-1L, TypeParser.parse("-1"));
        Assert.assertEquals(3424L, TypeParser.parse("3424"));
        Assert.assertEquals(43378373783L, TypeParser.parse("43378373783"));
        Assert.assertEquals(23277283278328328L, TypeParser.parse("23277283278328328"));
        Assert.assertEquals(-23277283278328328L, TypeParser.parse("-23277283278328328"));
    }

    @Test
    public void testByte() {
        Assert.assertEquals(2L, TypeParser.parse((byte)2));
    }

    @Test
    public void testShort() {
        Assert.assertEquals(2L, TypeParser.parse((short)2));
    }

    @Test
    public void testInteger() {
        Assert.assertEquals(2L, TypeParser.parse((int)2));
    }

    @Test
    public void testDouble() {
        Assert.assertEquals(2.123456789d, TypeParser.parse(2.123456789d));
        Assert.assertEquals(10.00000001d, TypeParser.parse(10.00000001d));
        Assert.assertEquals(223343.5654645d, TypeParser.parse(223343.5654645d));

    }

    @Test
    public void testDoubleAsString() {
        Assert.assertEquals(0.1d, TypeParser.parse("0.1"));
        Assert.assertEquals(-0.1d, TypeParser.parse("-0.1"));
        Assert.assertEquals(0.00000000002d, TypeParser.parse("0.00000000002"));
        Assert.assertEquals(1.343456799d, TypeParser.parse("1.343456799"));
        Assert.assertEquals(-1.343456799d, TypeParser.parse("-1.343456799"));
    }

    @Test
    public void testFloat() {
        Assert.assertEquals(2.1234567d, (Double) TypeParser.parse(2.1234567f), 0.0000001);
    }

    @Test
    public void testFloatAsString() {
        Assert.assertEquals(2.123456789d, TypeParser.parse("2.123456789"));
    }

    @Test
    public void testUuid() {
        UUID uuid = UUID.randomUUID();
        Assert.assertEquals(uuid.toString(), TypeParser.parse(uuid));
    }

    @Test
    public void testString() {
        Assert.assertEquals("Hello", TypeParser.parse("Hello"));
    }

}
