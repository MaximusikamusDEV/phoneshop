package com.es.phoneshop.web.controller.forms;

import com.es.phoneshop.web.constants.WebConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BulkCartItemForm {
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY_MESSAGE)
    private String phoneBrand;
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY_MESSAGE)
    private String phoneModel;
    @NotNull(message = WebConstants.QUANTITY_NULL_VALID_MESSAGE)
    @Min(value = 1,
            message = WebConstants.QUANTITY_MIN_VALID_MESSAGE)
    @Max(value = 5, message = WebConstants.QUANTITY_MAX_VALID_MESSAGE)
    private Integer quantity;

    public BulkCartItemForm() {}

    public BulkCartItemForm(String phoneBrand, String phoneModel, Integer quantity) {
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.quantity = quantity;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
