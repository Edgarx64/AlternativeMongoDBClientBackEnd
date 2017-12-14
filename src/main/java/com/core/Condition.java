package com.core;

import com.enums.Inequality;

public class Condition {
    private String field;
    private Inequality inequality;
    private String value;

    public Condition(String field, Inequality inequality, String value) {
        this.field = field;
        this.inequality = inequality;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Inequality getInequality() {
        return inequality;
    }

    public void setInequality(Inequality inequality) {
        this.inequality = inequality;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
