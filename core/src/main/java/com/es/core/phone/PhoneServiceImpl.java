package com.es.core.phone;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Resource
    private PhoneDao phoneDao;

    @Override
    public Phone getPhoneById(Long id) throws ItemNotExistException {
        Optional<Phone> phone = phoneDao.get(id);

        return phone.orElseThrow(() -> new ItemNotExistException(ExceptionConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE));
    }

    @Override
    public Phone getPhoneByBrandAndModel(String brand, String model) throws ItemNotExistException {
        Optional<Phone> phone = phoneDao.getByBrandAndModel(brand, model);

        return phone.orElseThrow(() -> new ItemNotExistException(ExceptionConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE));
    }

    @Override
    public List<Phone> getAllPhones(int page, String query, String sortField, String sortOrder) {
        List<Phone> phones;
        int offset = (page - 1) * ExceptionConstants.PHONE_PAGE_AMOUNT;

        phones = phoneDao.findAllInStockSorted(query,
                offset,
                ExceptionConstants.PHONE_PAGE_AMOUNT,
                sortField,
                sortOrder);

        return phones;
    }

    @Override
    public int getTotalPageQuantity(String query) {
        return (int) Math.ceil((double) phoneDao.getCountPhoneInStock(query) / ExceptionConstants.PHONE_PAGE_AMOUNT);
    }
}
