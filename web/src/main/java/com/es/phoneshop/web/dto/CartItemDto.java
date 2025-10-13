package com.es.phoneshop.web.dto;
import com.es.phoneshop.web.constants.WebConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemDto {

    @NotNull(message = WebConstants.PHONE_ID_VALID_MESSAGE)
    @Min(value = 1L,
            message = WebConstants.PHONE_ID_VALID_MESSAGE)
    private Long phoneId;

    @NotNull
    @Min(value = 1,
            message = WebConstants.QUANTITY_VALID_MESSAGE)
    private int quantity;

    public CartItemDto() {}

    public CartItemDto(Long phoneId, int quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
