package com.es.phoneshop.web.enums;

public enum SortField {
    ID("p.id"),
    BRAND("p.brand"),
    MODEL("p.model"),
    DISPLAY_SIZE_INCHES("cast(p.displaySizeInches as float)"),
    PRICE("cast(p.price as float)");

    private final String code;

    SortField(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SortField valueOfCode(String code) {
        for (SortField sortField : values()) {
            if(sortField.name().equals(code.toUpperCase())) {
                return sortField;
            }
        }
        return ID;
    }
}
