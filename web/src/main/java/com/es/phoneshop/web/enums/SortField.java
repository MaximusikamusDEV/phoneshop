package com.es.phoneshop.web.enums;

public enum SortField {
    ID("p.id"),
    BRAND("p.brand"),
    MODEL("p.model"),
    DISPLAY_SIZE_INCHES("cast(p.displaySizeInches as float)"),
    PRICE("cast(p.price as float)");

    private final String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
