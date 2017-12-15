package com.enums;

import com.exception.ParseEnumRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SQLKeywordTest {

    @Test
    public void parse() {
        assertEquals(SQLKeyword.parse("ORDER BY"), SQLKeyword.ORDER_BY);
    }

    @Test(expected = ParseEnumRuntimeException.class)
    public void parseException() {
        assertEquals(SQLKeyword.parse("ORDER_BY"), SQLKeyword.ORDER_BY);
    }

    @Test
    public void getKeyword() {
        assertEquals(SQLKeyword.SELECT.getKeyword(), "SELECT");
    }
}