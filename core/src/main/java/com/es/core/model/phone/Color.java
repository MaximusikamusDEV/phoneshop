package com.es.core.model.phone;

import java.util.Objects;

public class Color {
    private Long id;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Color color = (Color) object;
        return Objects.equals(id, color.id) && Objects.equals(code, color.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }
}
