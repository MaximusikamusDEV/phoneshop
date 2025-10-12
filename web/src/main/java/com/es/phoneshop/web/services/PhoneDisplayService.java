package com.es.phoneshop.web.services;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import java.util.List;

public interface PhoneDisplayService {
    Phone getPhoneById(Long id) throws ItemNotExistException;

    List<Phone> getAllPhones(int page, String query, String sortField, String sortOrder);

    int getTotalPageQuantity(String query);
}
