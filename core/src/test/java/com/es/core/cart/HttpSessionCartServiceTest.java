package com.es.core.cart;

import com.es.core.cart.Exceptions.ItemNotExsistException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Map;
import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
public class HttpSessionCartServiceTest {
    @Autowired
    private PhoneDao phoneDao;
    @Autowired
    HttpSessionCartService httpSessionCartService;
    private static Phone createdPhone;

    @BeforeAll
    static void setCreatedPhone() {
        createdPhone = new Phone();
        createdPhone.setBrand("ARCHOSTEST");
        createdPhone.setModel("ARCHOS 101 G9");
        createdPhone.setPrice(null);
        createdPhone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        createdPhone.setWeightGr(482);
        createdPhone.setLengthMm(BigDecimal.valueOf(276.0));
        createdPhone.setWidthMm(BigDecimal.valueOf(167.0));
        createdPhone.setHeightMm(BigDecimal.valueOf(12.6));
        createdPhone.setAnnounced(null);
        createdPhone.setDeviceType("Tablet");
        createdPhone.setOs("Android (4.0)");
        createdPhone.setDisplayResolution("1280 x  800");
        createdPhone.setDisplayTechnology(null);
        createdPhone.setBackCameraMegapixels(BigDecimal.valueOf(149));
        createdPhone.setFrontCameraMegapixels(BigDecimal.valueOf(1.3));
        createdPhone.setRamGb(null);
        createdPhone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        createdPhone.setBatteryCapacityMah(null);
        createdPhone.setTalkTimeHours(null);
        createdPhone.setStandByTimeHours(null);
        createdPhone.setBluetooth("2.1, EDR");
        createdPhone.setPositioning("GPS");
        createdPhone.setImageUrl("manufacturer/ARCHOS/ARCHOS 101 G9.jpg");
        createdPhone.setDescription("The ARCHOS 101 G9 description");
    }

    @Test
    void testGetCart() {
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
    }

    @Test
    void testAddPhone() {
        httpSessionCartService.addPhone(createdPhone.getId(), 1L);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getCart().get(createdPhone.getId()) == 1L);
    }

    @Test
    void testAddExistingPhone() {
        httpSessionCartService.addPhone(createdPhone.getId(), 1L);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getCart().get(createdPhone.getId()) == 1L);
        httpSessionCartService.addPhone(createdPhone.getId(), 1L);
        cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getCart().get(createdPhone.getId()) == 2L);
    }

    @Test
    void testUpdate() {
        Map<Long, Long> newCart = new HashMap<>();
        newCart.put(1L, 1L);
        assertNotEquals(newCart, httpSessionCartService.getCart());
        httpSessionCartService.update(newCart);
        assertEquals(httpSessionCartService.getCart().getCart().get(1L), newCart.get(1L));
    }

    @Test
    void getEmptyCart() {
        HashMap<Long, Long> emptyCart = new HashMap<>();
        httpSessionCartService.update(emptyCart);
        int quantity = 5;
        quantity = httpSessionCartService.getCartQuantity();
        assertEquals(0, quantity);
    }

    @Test
    void testRemove() throws ItemNotExsistException {
        Map<Long, Long> newCart = new HashMap<>();
        httpSessionCartService.update(newCart);
        httpSessionCartService.addPhone(1L, 1L);
        httpSessionCartService.addPhone(5L, 2L);
        Cart cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertEquals(1L, (long) cart.getCart().get(1L));
        httpSessionCartService.remove(1L);
        cart = httpSessionCartService.getCart();
        assertNotNull(cart);
        assertNull(cart.getCart().get(1L));
    }

    @Test
    void testRemoveWithException() {
        createdPhone.setId(10L);
        assertThrows(ItemNotExsistException.class, () -> httpSessionCartService.remove(createdPhone.getId()));
    }

    @Test
    void testGetCartQuantity() {
        Map<Long, Long> newCart = new HashMap<>();
        newCart.put(1L, 1L);
        httpSessionCartService.update(newCart);
        int quantity = httpSessionCartService.getCartQuantity();
        assertEquals(quantity, 1);

        Map<Long, Long> newCart2 = new HashMap<>();
        newCart2.put(1L, 1L);
        newCart2.put(2L, 1L);
        newCart2.put(3L, 1L);
        httpSessionCartService.update(newCart2);
        quantity = httpSessionCartService.getCartQuantity();
        assertEquals(quantity, 3);
    }

    @Test
    void testGetTotalCost() {
        createdPhone.setPrice(BigDecimal.valueOf(1000));
        createdPhone.setId(1L);
        httpSessionCartService.addPhone(createdPhone.getId(), 1L);
        assertEquals(BigDecimal.ZERO, httpSessionCartService.getTotalCost());
        createdPhone.setPrice(BigDecimal.valueOf(2000));
        createdPhone.setId(1939L);
        createdPhone.setPrice(BigDecimal.valueOf(3000));
        phoneDao.save(createdPhone);
        httpSessionCartService.addPhone(createdPhone.getId(), 3L);
        assertEquals(BigDecimal.valueOf(9000.0), httpSessionCartService.getTotalCost());

    }

}
