package com.es.phoneshop.web.dto;

import jakarta.validation.Valid;
import java.util.List;

public class CartForm {
    @Valid
    private List<CartItemForm> items;

    public CartForm(List<CartItemForm> items) {
        this.items = items;
    }

    public CartForm() {}

    public List<CartItemForm> getItems() {
        return items;
    }

    public void setItems(List<CartItemForm> items) {
        this.items = items;
    }
}
