package com.enums;

import com.exception.ParseEnumRuntimeException;

public enum SQLKeyword {
    SELECT("SELECT"),
    FROM("FROM"),
    WHERE("WHERE"),
    GROUP_BY("GROUP BY"),
    ORDER_BY("ORDER BY"),
    SKIP("SKIP"),
    LIMIT("LIMIT");

    private final String keyword;

    SQLKeyword(String keyword) {
        this.keyword = keyword;
    }

    public static SQLKeyword parse(String s) {
        for (SQLKeyword kw : SQLKeyword.values()) {
            if (kw.getKeyword().equals(s)) {
                return kw;
            }
        }
        throw new ParseEnumRuntimeException("SQL keyword could not parsing " + s);
    }

    public String getKeyword() {
        return keyword;
    }
}