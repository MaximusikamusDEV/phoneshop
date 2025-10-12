package com.es.phoneshop.web.services;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.constants.WebConstants;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PhoneDisplayServiceImpl implements PhoneDisplayService {
    @Resource
    private PhoneDao phoneDao;

    @Override
    public Phone getPhoneById(Long id) throws ItemNotExistException {
        Optional<Phone> phone = phoneDao.get(id);

        return phone.orElseThrow(() -> new ItemNotExistException(WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE));
    }

    @Override
    public List<Phone> getAllPhones(int page, String query, String sortField, String sortOrder) {
        List<Phone> phones;
        int offset = (page - 1) * WebConstants.PHONE_PAGE_AMOUNT;

        phones = phoneDao.findAllInStockSorted(query,
                offset,
                WebConstants.PHONE_PAGE_AMOUNT,
                sortField,
                sortOrder);

        return phones;
    }

    @Override
    public int getTotalPageQuantity(String query) {
        return (int) Math.ceil((double) phoneDao.getCountPhoneInStock(query) / WebConstants.PHONE_PAGE_AMOUNT);
    }
}
