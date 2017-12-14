package com.enums;

import com.exception.ParseEnumRuntimeException;

public enum DirectSort {
    ASC,
    DESC;

    public static DirectSort parse(String s) {
        String toUpperCase = s.toUpperCase();
        if (toUpperCase.equals("ASC")) {
            return DirectSort.ASC;
        }
        else if (toUpperCase.equals("DESC")) {
            return DirectSort.DESC;
        }
        throw new ParseEnumRuntimeException("Direct sort could not parsing " + s);
    }
}
