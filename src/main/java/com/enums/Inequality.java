package com.enums;

import com.exception.ParseEnumRuntimeException;

public enum Inequality {
    EQUALITY("", "="),
    LESS_THAN("$lt", "<"),
    LESS_THAN_EQUALS("$lte", "<="),
    GREATER_THAN("$gt", ">"),
    GREATER_THAN_EQUALS("$gte", ">="),
    NOT_EQUALS("$ne", "<>");

    private final String symbolMongo;
    private final String symbolStandard;

    Inequality(String symbolMongo, String symbolStandard) {
        this.symbolMongo = symbolMongo;
        this.symbolStandard = symbolStandard;
    }

    public static Inequality parse(String s) {
        for (Inequality inequality : Inequality.values()) {
            if (s.equals(inequality.symbolStandard)) {
                return inequality;
            }
        }
        throw new ParseEnumRuntimeException("Inequality could not parsing " + s);
    }

    public String getSymbolMongo() {
        return symbolMongo;
    }

    public String getSymbolStandard() {
        return symbolStandard;
    }
}
