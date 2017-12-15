package com.enums;

import com.exception.ParseEnumRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectSortTest {

    @Test
    public void parseASCLowerCase() {
        assertEquals(DirectSort.parse("asc"), DirectSort.ASC);
    }

    @Test
    public void parseASCUpperCase() {
        assertEquals(DirectSort.parse("ASC"), DirectSort.ASC);
    }

    @Test(expected = ParseEnumRuntimeException.class)
    public void parseASCException() {
        DirectSort.parse("ASK");
    }

    @Test
    public void parseDESCLowerCase() {
        assertEquals(DirectSort.parse("desc"), DirectSort.DESC);
    }

    @Test
    public void parseDESCUpperCase() {
        assertEquals(DirectSort.parse("DESC"), DirectSort.DESC);
    }

    @Test(expected = ParseEnumRuntimeException.class)
    public void parseDESCException() {
        DirectSort.parse("DESK");
    }

}