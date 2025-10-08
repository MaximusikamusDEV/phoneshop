package com.es.core.model.phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    void save(Phone phone);
    List<Phone> findAll(int offset, int limit);
    List<Phone> findAllInStockSorted(int offset, int limit, String sortField, String sortOrder);
    int getPhoneInStockCount();
    List<Phone> findAllByQueryInStock(String query, int offset, int limit, String sortField, String sortOrder);
    int getCountPhoneByQueryInStock(String query);
}
