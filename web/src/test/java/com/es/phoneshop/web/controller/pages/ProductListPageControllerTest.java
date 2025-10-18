package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.enums.SortField;
import com.es.phoneshop.web.enums.SortOrder;
import com.es.phoneshop.web.exceptions.InvalidPageNumberException;
import com.es.phoneshop.web.services.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ProductListPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private PhoneService phoneService;
    @InjectMocks
    private ProductListPageController productListPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowProductListPage() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        int page = 1;
        String query = "query";
        List<Phone> phones = new ArrayList<>();
        String sortField = "brand";
        String sortOrder = "asc";

        when(phoneService.getTotalPageQuantity("%query%")).thenReturn(100);
        when(cartService.getCart()).thenReturn(cart);
        when(phoneService.getAllPhones(page, "%query%", SortField.valueOfCode(sortField).getCode(),
                SortOrder.valueOfCode(sortOrder).getCode())).thenReturn(phones);

        String response = productListPageController.showProductList(page, query, sortField, sortOrder, model);

        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(phones, model.getAttribute(WebConstants.PHONES_ATTR));
        assertEquals(100, model.getAttribute(WebConstants.TOTAL_PAGES_ATTR));
        assertEquals("productList", response);
    }

    @Test
    void testShowProductListPageEmptyQuery() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        int page = 1;
        String query = null;
        List<Phone> phones = new ArrayList<>();
        String sortField = "brand";
        String sortOrder = "asc";

        when(phoneService.getTotalPageQuantity(null)).thenReturn(100);
        when(cartService.getCart()).thenReturn(cart);
        when(phoneService.getAllPhones(page, null, SortField.valueOfCode(sortField).getCode(),
                SortOrder.valueOfCode(sortOrder).getCode())).thenReturn(phones);

        String response = productListPageController.showProductList(page, query, sortField, sortOrder, model);

        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(phones, model.getAttribute(WebConstants.PHONES_ATTR));
        assertEquals(100, model.getAttribute(WebConstants.TOTAL_PAGES_ATTR));
        assertEquals("productList", response);
    }

    @Test
    void testInvalidPageNumber() {
        Model model = new ExtendedModelMap();
        int page = -5;
        String query = "query";
        String sortField = "brand";
        String sortOrder = "asc";

        when(phoneService.getTotalPageQuantity("%query%")).thenReturn(100);

        assertThrows(InvalidPageNumberException.class, () -> productListPageController.showProductList(page, query, sortField, sortOrder, model));
    }

    @Test
    void testZeroTotalQuantity() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        int page = 1;
        String query = "query";
        List<Phone> phones = new ArrayList<>();
        String sortField = "brand";
        String sortOrder = "asc";

        when(phoneService.getTotalPageQuantity("%query%")).thenReturn(0);
        when(cartService.getCart()).thenReturn(cart);
        when(phoneService.getAllPhones(page, "%query%", SortField.valueOfCode(sortField).getCode(),
                SortOrder.valueOfCode(sortOrder).getCode())).thenReturn(phones);

        String response = productListPageController.showProductList(page, query, sortField, sortOrder, model);

        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(phones, model.getAttribute(WebConstants.PHONES_ATTR));
        assertEquals(0, model.getAttribute(WebConstants.TOTAL_PAGES_ATTR));
        assertEquals("productList", response);
    }
}