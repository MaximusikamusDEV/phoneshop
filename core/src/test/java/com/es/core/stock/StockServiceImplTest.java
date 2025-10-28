package com.es.core.stock;

import com.es.core.model.phone.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
@WebAppConfiguration
@Transactional
public class StockServiceImplTest {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockService stockService;

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

    void savePhoneStock(Phone phone, int stock, int reserved) {
        Stock phoneStock = new Stock();
        phoneStock.setStock(stock);
        phoneStock.setReserved(reserved);
        phoneStock.setPhone(phone);
        stockService.setStock(phoneStock);
    }

    @Test
    void testReservePhone() {
        Phone phone = setCreatedPhone();
        phoneDao.save(phone);
        savePhoneStock(phone, 10, 1);
        assertEquals(1, (int) stockService.getStock(phone).getReserved());
        assertEquals(10, (int) stockService.getStock(phone).getStock());
        assertEquals(stockService.getStock(phone).getPhone(), phone);

        stockService.reservePhone(phone, 5);
        assertEquals(6, (int) stockService.getStock(phone).getReserved());
        assertEquals(5, (int) stockService.getStock(phone).getStock());
        assertEquals(stockService.getStock(phone).getPhone(), phone);
    }

    @Test
    void testGetStock() {
        Phone phone = setCreatedPhone();
        phoneDao.save(phone);
        savePhoneStock(phone, 10, 1);
        assertEquals(1, (int) stockService.getStock(phone).getReserved());
        assertEquals(10, (int) stockService.getStock(phone).getStock());
        assertEquals(stockService.getStock(phone).getPhone(), phone);
    }

    @Test
    void testSetStock() {
        Phone phone = setCreatedPhone();
        phoneDao.save(phone);
        savePhoneStock(phone, 10, 1);

    }

    @Test
    void testIsPhoneInStock() {
        Phone phone = setCreatedPhone();
        phoneDao.save(phone);
        savePhoneStock(phone, 10, 1);
        assertEquals(1, (int) stockService.getStock(phone).getReserved());
        assertEquals(10, (int) stockService.getStock(phone).getStock());
        assertEquals(stockService.getStock(phone).getPhone(), phone);
        assertTrue(stockService.isPhoneInStock(phone, 1));
        assertFalse(stockService.isPhoneInStock(phone, 2000));
    }
}
