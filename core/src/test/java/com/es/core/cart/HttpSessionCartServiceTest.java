package com.es.core.cart;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import com.es.core.order.OutOfStockException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
@WebAppConfiguration
@Transactional
public class HttpSessionCartServiceTest {
    @Autowired
    private PhoneDao phoneDao;
    @Autowired
    HttpSessionCartService httpSessionCartService;
    @Autowired
    private Cart cart;
    @Autowired
    private StockDao stockDao;
    private MockHttpSession session;
    private Phone createdPhone;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        createdPhone = setCreatedPhone();
    }

    @AfterEach
    void clear() {
        RequestContextHolder.resetRequestAttributes();
    }

    Phone setCreatedPhone() {
        Phone phone = new Phone();
        phone.setBrand("ARCHOSTEST");
        phone.setModel("ARCHOS 101 G9");
        phone.setPrice(null);
        phone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        phone.setWeightGr(482);
        phone.setLengthMm(BigDecimal.valueOf(276.0));
        phone.setWidthMm(BigDecimal.valueOf(167.0));
        phone.setHeightMm(BigDecimal.valueOf(12.6));
        phone.setAnnounced(null);
        phone.setDeviceType("Tablet");
        phone.setOs("Android (4.0)");
        phone.setDisplayResolution("1280 x  800");
        phone.setDisplayTechnology(null);
        phone.setBackCameraMegapixels(BigDecimal.valueOf(149));
        phone.setFrontCameraMegapixels(BigDecimal.valueOf(1.3));
        phone.setRamGb(null);
        phone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        phone.setBatteryCapacityMah(null);
        phone.setTalkTimeHours(null);
        phone.setStandByTimeHours(null);
        phone.setBluetooth("2.1, EDR");
        phone.setPositioning("GPS");
        phone.setImageUrl("manufacturer/ARCHOS/ARCHOS 101 G9.jpg");
        phone.setDescription("The ARCHOS 101 G9 description");

        return phone;
    }

    void savePhoneWithStock(Phone phone, int stock, int reserved) {
        phoneDao.save(phone);

        Stock phoneStock = new Stock();
        phoneStock.setStock(stock);
        phoneStock.setReserved(reserved);
        phoneStock.setPhone(phone);
        stockDao.setStock(phoneStock);
    }

    @Test
    void testGetCart() {
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
    }

    @Test
    void testAddPhone() throws ItemNotExistException, OutOfStockException {
        createdPhone.setId(1L);
        savePhoneWithStock(createdPhone, 100, 0);

        httpSessionCartService.addPhone(createdPhone.getId(), 1);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertEquals(createdPhone.getId(), cart.getCartItems().get(0).getPhone().getId());
    }

    @Test
    void testAddExistingPhone() throws ItemNotExistException, OutOfStockException {
        createdPhone.setId(1L);
        createdPhone.setId(1L);
        savePhoneWithStock(createdPhone, 100, 0);

        httpSessionCartService.addPhone(createdPhone.getId(), 1);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getCartItems().get(0).getQuantity() == 1);
        httpSessionCartService.addPhone(createdPhone.getId(), 5);
        cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getCartItems().get(0).getQuantity() == 6);
        assertEquals(createdPhone.getId(), cart.getCartItems().get(0).getPhone().getId());
    }

    @Test
    void testUpdate() throws OutOfStockException {
        List<CartItem> newCartItems = new ArrayList<>();
        Cart newCart = new Cart();
        newCart.setCartItems(newCartItems);
        assertNotEquals(newCart, httpSessionCartService.getCart());
        httpSessionCartService.update(newCartItems);
        assertEquals(httpSessionCartService.getCart().getCartItems(), newCartItems);
    }

    @Test
    void testUpdateWithPhones() throws OutOfStockException {
        List<CartItem> newCartItems = new ArrayList<>();

        createdPhone.setId(1L);
        createdPhone.setPrice(BigDecimal.valueOf(10.1));
        createdPhone.setId(1L);
        savePhoneWithStock(createdPhone, 100, 0);

        Long phoneId = createdPhone.getId();
        createdPhone.setId(phoneId);

        newCartItems.add(new CartItem(createdPhone, 5));

        Cart newCart = new Cart();
        newCart.setCartItems(newCartItems);
        assertNotEquals(newCart, httpSessionCartService.getCart());
        httpSessionCartService.update(newCartItems);
        assertEquals(httpSessionCartService.getCart().getCartItems(), newCartItems);

        assertEquals(BigDecimal.valueOf(50.5), httpSessionCartService.getCart().getTotalCost());
    }

    @Test
    void getEmptyCart() throws OutOfStockException {
        List<CartItem> newCartItems = new ArrayList<>();
        Cart newCart = new Cart();
        newCart.setCartItems(newCartItems);
        httpSessionCartService.update(newCartItems);
        int quantity = 5;
        quantity = httpSessionCartService.getCart().getTotalQuantity();
        assertEquals(0, quantity);
    }

    @Test
    void testRemove() throws ItemNotExistException, OutOfStockException {
        List<CartItem> newCartItems = new ArrayList<>();
        Cart newCart = new Cart();
        newCart.setCartItems(newCartItems);
        httpSessionCartService.update(newCartItems);

        createdPhone.setId(1L);
        savePhoneWithStock(createdPhone, 100, 0);
        Long firstPhoneId = createdPhone.getId();

        createdPhone.setModel("ARCHOS 101 G9test");
        createdPhone.setBrand("ARCHOS 101 G9test");
        createdPhone.setId(2L);
        savePhoneWithStock(createdPhone, 100, 0);
        Long secondPhoneId = createdPhone.getId();

        httpSessionCartService.addPhone(firstPhoneId, 1);
        httpSessionCartService.addPhone(secondPhoneId, 2);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertEquals(1, cart.getCartItems().get(0).getQuantity());
        assertEquals(2, cart.getCartItems().get(1).getQuantity());
        httpSessionCartService.remove(firstPhoneId);
        cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertEquals(2, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    void testRemoveWithException() {
        createdPhone.setId(10L);
        assertThrows(ItemNotExistException.class, () -> httpSessionCartService.remove(createdPhone.getId()));
    }

    @Test
    void testGetTotalPrice() throws ItemNotExistException, OutOfStockException {
        List<CartItem> newCartItems = new ArrayList<>();
        Cart newCart = new Cart();
        newCart.setCartItems(newCartItems);
        httpSessionCartService.update(newCartItems);

        createdPhone.setId(1L);
        createdPhone.setPrice(BigDecimal.valueOf(10));
        savePhoneWithStock(createdPhone, 100, 0);
        Long firstPhoneId = createdPhone.getId();

        createdPhone.setModel("ARCHOS 101 G9test");
        createdPhone.setBrand("ARCHOS 101 G9test");
        createdPhone.setPrice(BigDecimal.valueOf(20));
        createdPhone.setId(2L);
        savePhoneWithStock(createdPhone, 100, 0);
        Long secondPhoneId = createdPhone.getId();

        httpSessionCartService.addPhone(firstPhoneId, 1);
        httpSessionCartService.addPhone(secondPhoneId, 2);

        assertEquals(BigDecimal.valueOf(50.0), cart.getTotalCost());
    }
}
