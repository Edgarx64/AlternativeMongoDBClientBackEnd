package com.enums;

import com.exception.ParseEnumRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class InequalityTest {

    @Test
    public void parseEquality() {
        assertEquals(Inequality.parse("="), Inequality.EQUALITY);
    }

    @Test
    public void parseLessThan() {
        assertEquals(Inequality.parse("<"), Inequality.LESS_THAN);
    }

    @Test
    public void parseLessThanEquals() {
        assertEquals(Inequality.parse("<="), Inequality.LESS_THAN_EQUALS);
    }

    @Test
    public void parseGreaterThan() {
        assertEquals(Inequality.parse(">"), Inequality.GREATER_THAN);
    }

    @Test
    public void parseGreaterThanEquals() {
        assertEquals(Inequality.parse(">="), Inequality.GREATER_THAN_EQUALS);
    }

    @Test
    public void parseNotEquals() {
        assertEquals(Inequality.parse("<>"), Inequality.NOT_EQUALS);
    }

    @Test(expected = ParseEnumRuntimeException.class)
    public void parseException() {
        assertEquals(Inequality.parse("!="), Inequality.NOT_EQUALS);
    }

    @Test
    public void getSymbolMongo() {
        assertEquals(Inequality.GREATER_THAN_EQUALS.getSymbolMongo(), "$gte");
    }

    @Test
    public void getSymbolStandard() {
        assertEquals(Inequality.LESS_THAN_EQUALS.getSymbolStandard(), "<=");
    }
}