package com.es.core.model.phone;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
@Transactional
public class JdbcStockDaoIntTest {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

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
        stockDao.saveStock(phoneStock);
    }

    @Test
    void testSetAndGetStock() {
        Phone phone = setCreatedPhone();
        phoneDao.save(phone);
        savePhoneStock(phone, 10, 1);
        assertEquals(1, (int) stockDao.getStock(phone).getReserved());
        assertEquals(10, (int) stockDao.getStock(phone).getStock());
        assertEquals(stockDao.getStock(phone).getPhone(), phone);

        savePhoneStock(phone, 15, 5);
        assertEquals(5, (int) stockDao.getStock(phone).getReserved());
        assertEquals(15, (int) stockDao.getStock(phone).getStock());
        assertEquals(stockDao.getStock(phone).getPhone(), phone);
    }
}
