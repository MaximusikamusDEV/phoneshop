package com.es.phoneshop.web.services;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.enums.SortField;
import com.es.phoneshop.web.enums.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class PhoneServiceTest {
    @Mock
    private PhoneDao phoneDao;
    @InjectMocks
    private PhoneServiceImpl phoneDisplayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhoneById() {
        Long id = 1L;
        Phone phone = new Phone();

        when(phoneDao.get(id)).thenReturn(Optional.of(phone));

        Phone result = phoneDisplayService.getPhoneById(id);

        assertEquals(phone, result);
    }

    @Test
    void testGetTotalPageQuantity() {
        String query = "query";

        when(phoneDao.getCountPhoneInStock(query)).thenReturn(100);

        int result = phoneDisplayService.getTotalPageQuantity(query);

        assertEquals(10, result);
    }

    @Test
    void testGetAllPhones() {
        String query = "query";
        int page = 1;
        String sortField = SortField.PRICE.getCode();
        String sortOrder = SortOrder.ASC.getCode();
        List<Phone> phones = new ArrayList<>();
        int offset = (page - 1) * WebConstants.PHONE_PAGE_AMOUNT;

        when(phoneDao.findAllInStockSorted(query, offset, WebConstants.PHONE_PAGE_AMOUNT, sortField, sortOrder)).thenReturn(phones);

        List<Phone> result = phoneDisplayService.getAllPhones(page, query, sortField, sortOrder);

        assertEquals(phones, result);
    }
}
