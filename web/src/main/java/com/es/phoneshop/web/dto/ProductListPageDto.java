package com.es.phoneshop.web.dto;

import com.es.core.model.phone.Phone;
import java.util.List;

public class ProductListPageDto {
    List<Phone> phones;
    int totalPages;

    public ProductListPageDto() {
    }

    public ProductListPageDto(List<Phone> phones, int totalPages) {
        this.phones = phones;
        this.totalPages = totalPages;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
