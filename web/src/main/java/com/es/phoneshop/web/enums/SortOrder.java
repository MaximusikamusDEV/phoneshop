package com.es.phoneshop.web.enums;

public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String code;

    SortOrder(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SortOrder valueOfCode(String code) {
        for (SortOrder sortOrder : values()) {
            if (sortOrder.code.equals(code)) {
                return sortOrder;
            }
        }
        return ASC;
    }
}
