package com.es.phoneshop.web.controller.forms;

import com.es.phoneshop.web.constants.WebConstants;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

public class OrderForm {
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY)
    private String firstName;
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY)
    private String lastName;
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY)
    private String deliveryAddress;
    @NotBlank(message = WebConstants.FIELD_CANT_BE_EMPTY)
    @Pattern(regexp = "^\\+375\\d{9}$", message = WebConstants.INVALID_PHONE_NUMBER)
    private String contactPhoneNo;
    @Size(max = 900, message = WebConstants.TOO_HIGH_ADDITIONAL_INFO)
    private String additionalInfo;
    @PastOrPresent
    private LocalDateTime createdAt;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
