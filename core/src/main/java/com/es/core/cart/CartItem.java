package com.es.core.cart;

import com.es.core.model.phone.Phone;

public class CartItem {
    Phone phone;
    int quantity;

    public CartItem(Phone phone, int quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public CartItem(Phone phone) {
        this.phone = phone;
        this.quantity = 0;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
