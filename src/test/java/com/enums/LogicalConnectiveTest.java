package com.enums;

import com.exception.ParseEnumRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogicalConnectiveTest {

    @Test
    public void parseANDLowerCase() {
        assertEquals(LogicalConnective.parse("and"), LogicalConnective.AND);
    }

    @Test
    public void parseANDUpperCase() {
        assertEquals(LogicalConnective.parse("AND"), LogicalConnective.AND);
    }

    @Test
    public void parseORLowerCase() {
        assertEquals(LogicalConnective.parse("or"), LogicalConnective.OR);
    }

    @Test
    public void parseORUpperCase() {
        assertEquals(LogicalConnective.parse("OR"), LogicalConnective.OR);
    }

    @Test
    public void parseNONE() {
        assertEquals(LogicalConnective.parse(""), LogicalConnective.NONE);
    }

    @Test(expected = ParseEnumRuntimeException.class)
    public void parseException() {
        assertEquals(LogicalConnective.parse("asc"), LogicalConnective.AND);
    }

    @Test
    public void getSymbolMongo() {
        assertEquals(LogicalConnective.AND.getSymbolMongo(), "$and");
    }

    @Test
    public void getLogicalStandard() {
        assertEquals(LogicalConnective.OR.getLogicalStandard(), "OR");
    }
}