package com.es.core.cart;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemDto {

    @Min(value = 1L, message = "Phone id can't be lower than 1")
    @NotNull(message = "Phone id can't be null")
    private Long phoneId;

    @NotNull(message = "Quantity can't be empty")
    @Min(value = 1, message = "Quantity need to be at least 1")
    private Long quantity;

    public CartItemDto() {}

    public CartItemDto(Long phoneId, Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
