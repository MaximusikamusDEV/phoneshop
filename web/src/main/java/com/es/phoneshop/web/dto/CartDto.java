package com.es.phoneshop.web.dto;

import jakarta.validation.Valid;
import java.util.List;

public class CartDto {
    @Valid
    private List<CartItemDto> items;

    public CartDto(List<CartItemDto> items) {
        this.items = items;
    }

    public CartDto() {
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }
}
