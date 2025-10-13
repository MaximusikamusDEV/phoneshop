package com.es.phoneshop.web.services;

import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.ProductListPageDto;
import com.es.phoneshop.web.enums.SortField;
import com.es.phoneshop.web.enums.SortOrder;
import com.es.phoneshop.web.exceptions.InvalidPageNumber;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PhoneDisplayService {
    @Resource
    private PhoneDao phoneDao;

    public ProductListPageDto getProductListPageDto(int page, String query, String sortField, String sortOrder) {
        ProductListPageDto productListPageDto = new ProductListPageDto();
        String validatedSortField = validateSortField(sortField);
        String validatedSortOrder = validateSortOrder(sortOrder);
        int offset = (page - 1) * WebConstants.PHONE_PAGE_AMOUNT;

        if (query != null && !query.isEmpty()) {
            String preparedQuery = "%" + query.toLowerCase() + "%";
            int totalPages = (int) Math.ceil((double) phoneDao.getCountPhoneInStock(Optional.of(preparedQuery)) / WebConstants.PHONE_PAGE_AMOUNT);

            validatePage(page, totalPages);

            productListPageDto.setTotalPages(totalPages);
            productListPageDto.setPhones(
                    phoneDao.findAllInStockSorted(Optional.of(preparedQuery),
                            offset,
                            WebConstants.PHONE_PAGE_AMOUNT,
                            validatedSortField,
                            validatedSortOrder));
        } else {
            int totalPages = (int) Math.ceil((double) phoneDao.getCountPhoneInStock(Optional.empty()) / WebConstants.PHONE_PAGE_AMOUNT);

            validatePage(page, totalPages);

            productListPageDto.setTotalPages(totalPages);
            productListPageDto.setPhones(
                    phoneDao.findAllInStockSorted(Optional.empty(),
                            offset,
                            WebConstants.PHONE_PAGE_AMOUNT,
                            validatedSortField,
                            validatedSortOrder));
        }

        return productListPageDto;
    }

    private void validatePage(int page, int totalPages){
        if (page < 1 || page > totalPages) {
            throw new InvalidPageNumber(WebConstants.ERROR_INVALID_PAGE_NUMBER_MESSAGE);
        }
    }

    public String validateSortField(String sortField) {
        try {
            return SortField.valueOf(sortField.toUpperCase()).getFieldName();
        } catch (IllegalArgumentException e) {
            return SortField.ID.getFieldName();
        }
    }

    public String validateSortOrder(String sortOrder) {
        try {
            return SortOrder.valueOf(sortOrder.toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            return SortOrder.ASC.name();
        }
    }
}
