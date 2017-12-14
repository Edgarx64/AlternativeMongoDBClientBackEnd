package com.core;

import com.enums.DirectSort;

public class SortBy {
    private String field;
    private DirectSort directSort;

    public SortBy(String field, DirectSort directSort) {
        this.field = field;
        this.directSort = directSort;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public DirectSort getDirectSort() {
        return directSort;
    }

    public void setDirectSort(DirectSort directSort) {
        this.directSort = directSort;
    }
}
