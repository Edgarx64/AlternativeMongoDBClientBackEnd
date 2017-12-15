package com.enums;

import com.exception.ParseEnumRuntimeException;

public enum LogicalConnective {
    AND("$and", "AND"),
    OR("$or", "OR"),
    NONE("", "");

    private final String symbolMongo;
    private final String logicalStandard;

    LogicalConnective(String symbolMongo, String logicalStandard) {
        this.symbolMongo = symbolMongo;
        this.logicalStandard = logicalStandard;
    }

    public static LogicalConnective parse(String s) {
        String toUpperCase = s.toUpperCase();
        for (LogicalConnective lc : LogicalConnective.values()) {
            if (lc.logicalStandard.equals(toUpperCase)) {
                return lc;
            }
        }
        throw new ParseEnumRuntimeException("Logical connective could not parsing " + toUpperCase);
    }

    public String getSymbolMongo() {
        return symbolMongo;
    }

    public String getLogicalStandard() {
        return logicalStandard;
    }
}
