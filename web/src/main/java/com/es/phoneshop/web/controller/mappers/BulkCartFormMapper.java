package com.es.phoneshop.web.controller.mappers;

import com.es.core.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.phone.PhoneService;
import com.es.phoneshop.web.controller.forms.BulkCartForm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkCartFormMapper {
    @Resource
    private PhoneService phoneService;

    public List<CartItem> convertToCartItems(BulkCartForm bulkCartForm) {
        return bulkCartForm.getItems().stream()
                .map(bulkFormItem -> {
                    Phone phone = phoneService.getPhoneByBrandAndModel(
                            bulkFormItem.getPhoneBrand(),
                            bulkFormItem.getPhoneModel());
                    return new CartItem(phone, bulkFormItem.getQuantity());
                })
                .collect(Collectors.toList());
    }
}