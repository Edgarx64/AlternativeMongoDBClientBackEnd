package com.core;

import com.enums.LogicalConnective;

public class Expression {
    private Condition condition;
    private LogicalConnective logicalConnective;

    public Expression(Condition condition, LogicalConnective logicalConnective) {
        this.condition = condition;
        this.logicalConnective = logicalConnective;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public LogicalConnective getLogicalConnective() {
        return logicalConnective;
    }

    public void setLogicalConnective(LogicalConnective logicalConnective) {
        this.logicalConnective = logicalConnective;
    }

    @Override
    public String toString() {
        return "Expression{" +
                condition.getField() + " " +
                condition.getInequality().getSymbolStandard() + " " +
                condition.getValue() + " " +
                ((logicalConnective.equals(LogicalConnective.NONE)) ? "" : logicalConnective.getLogicalStandard()) +
                "}";
    }
}
