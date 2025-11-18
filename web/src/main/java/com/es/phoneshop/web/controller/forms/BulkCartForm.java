package com.es.phoneshop.web.controller.forms;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class BulkCartForm {
    @Valid
    private List<BulkCartItemForm> items;

    public BulkCartForm() {
        items = new ArrayList<>();
    }

    public BulkCartForm(List<BulkCartItemForm> items) {
        this.items = items;
    }

    public List<BulkCartItemForm> getItems() {
        return items;
    }

    public void setItems(List<BulkCartItemForm> items) {
        this.items = items;
    }
}
