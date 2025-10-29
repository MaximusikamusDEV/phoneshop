package com.es.phoneshop.web.controller.forms;
import com.es.phoneshop.web.constants.WebConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemForm {

    @NotNull(message = WebConstants.PHONE_ID_VALID_MESSAGE)
    @Min(value = 1L,
            message = WebConstants.PHONE_ID_VALID_MESSAGE)
    private Long phoneId;

    @NotNull(message = WebConstants.QUANTITY_NULL_VALID_MESSAGE)
    @Min(value = 1,
            message = WebConstants.QUANTITY_MIN_VALID_MESSAGE)
    @Max(value = 5, message = WebConstants.QUANTITY_MAX_VALID_MESSAGE)
    private Integer quantity;

    public CartItemForm() {}

    public CartItemForm(Long phoneId, Integer quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
