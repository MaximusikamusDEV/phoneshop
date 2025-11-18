package com.es.core.phone;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import java.util.List;

public interface PhoneService {
    Phone getPhoneById(Long id) throws ItemNotExistException;

    List<Phone> getAllPhones(int page, String query, String sortField, String sortOrder);

    int getTotalPageQuantity(String query);

    Phone getPhoneByBrandAndModel(String brand, String model) throws ItemNotExistException;
}
