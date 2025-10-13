package com.es.core.model.phone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
public class JdbcPhoneDaoIntTest {
    @Autowired
    private PhoneDao phoneDao;
    private static Phone createdPhone;
    private static Color createdColor;

    @BeforeAll
    static void setCreatedColor() {
        createdColor = new Color();
        createdColor.setId(1013L);
        createdColor.setCode("TEST");
    }

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
    void testFindAll() {
        List<Phone> phoneList = phoneDao.findAll(0, 5);

        assertNotNull(phoneList);
        assertEquals(5, phoneList.size());
        assertNotNull(phoneList.get(0).getColors());
        assertNotEquals(Collections.EMPTY_SET, phoneList.get(0).getColors());
    }

    @Test
    void testGet() {
        Optional<Phone> phone = phoneDao.get(1000L);

        assertNotNull(phone);
        assertEquals("ARCHOS", phone.get().getBrand());
        assertNotEquals(null, phone.get().getColors());
        assertFalse(phone.get().getColors().isEmpty());
        assertNotEquals(Collections.EMPTY_SET, phone.get().getColors());
    }

    @Test
    void testSave() {
        phoneDao.save(createdPhone);
        assertNotNull(createdPhone.getId());
        Optional<Phone> phoneGet = phoneDao.get(createdPhone.getId());
        assertEquals("ARCHOSTEST", phoneGet.get().getBrand());
    }

    @Test
    void testUpdate() {
        createdPhone.setDescription("New description");
        phoneDao.save(createdPhone);
        Optional<Phone> phoneUpdate = phoneDao.get(createdPhone.getId());
        assertTrue(phoneUpdate.isPresent());
        assertEquals("New description", phoneUpdate.get().getDescription());
    }

    @Test
    void getNonExistingPhone() {
        Optional<Phone> phone = phoneDao.get(-1L);
        assertTrue(phone.isEmpty());
    }

    @Test
    void testSaveWithColors() {
        Optional<Phone> phone = phoneDao.get(1000L);

        assertNotNull(phone);
        assertTrue(phone.isPresent());
        assertEquals("ARCHOS", phone.get().getBrand());
        assertNotEquals(null, phone.get().getColors());
        assertNotEquals(Collections.EMPTY_SET, phone.get().getColors());

        Set<Color> colorsSetEmpty = new HashSet<>();
        phone.get().setColors(colorsSetEmpty);
        phoneDao.save(phone.get());

        Optional<Phone> updatedPhoneEmpty = phoneDao.get(1000L);
        assertTrue(updatedPhoneEmpty.isPresent());
        assertEquals(0, updatedPhoneEmpty.get().getColors().size());

        Set<Color> colorsSetNotEmpty = new HashSet<>();
        colorsSetNotEmpty.add(createdColor);
        phone.get().setColors(colorsSetNotEmpty);
        phoneDao.save(phone.get());

        Optional<Phone> updatedPhoneNotEmpty = phoneDao.get(1000L);
        assertTrue(updatedPhoneNotEmpty.isPresent());
        assertEquals(1, updatedPhoneNotEmpty.get().getColors().size());
    }

    @Test
    void testSaveWithException() {
        Phone problemPhone = new Phone();
        problemPhone.setBrand(null);
        problemPhone.setModel("Test");

        assertThrows(DataIntegrityViolationException.class, () -> phoneDao.save(problemPhone));
    }

    @Test
    void testSavePhoneColors() {
        HashSet<Color> colorsSet = new HashSet<>();
        Color color = new Color();
        color.setCode("TESTCOLOR");
        colorsSet.add(color);
        createdPhone.setColors(colorsSet);
        phoneDao.save(createdPhone);
        Optional<Phone> phoneGet = phoneDao.get(createdPhone.getId());
        assertEquals(1, phoneGet.get().getColors().size());

        assertTrue(phoneGet.get().getColors().stream()
                .anyMatch(c -> color.getCode().equals(c.getCode())));
    }

    @Test
    void testFindPhoneByQueryCount() {
        int amountSamsung = phoneDao.getCountPhoneInStock(Optional.ofNullable("%samsung%"));
        int amountMeizu = phoneDao.getCountPhoneInStock(Optional.ofNullable("%meizu%"));
        assertNotEquals(amountSamsung, amountMeizu);
        assertTrue(amountSamsung > 1);
        assertTrue(amountMeizu > 1);
    }

    @Test
    void testFindPhoneByQuery() {
        String preparedQuery = "%" + "samsung" + "%";
        List<Phone> phones = phoneDao.findAllInStockSorted(Optional.of(preparedQuery), 10, 10, "p.brand", "asc");
        assertFalse(phones.isEmpty());

        for (Phone phone : phones) {
            assertTrue(phone.getBrand().toLowerCase().equals("samsung"));
        }
    }

    @Test
    void testGetCountPhoneInStock() {
        int amount = phoneDao.getCountPhoneInStock(Optional.empty());
        assertNotEquals(0, amount);
    }

    @Test
    void testFindAllInStockSorted() {
        List<Phone> phones1 = phoneDao.findAllInStockSorted(Optional.empty(), 0, 10, "p.brand", "asc");
        assertFalse(phones1.isEmpty());
        List<Phone> phones2 = phoneDao.findAllInStockSorted(Optional.empty(), 0, 10, "p.brand", "desc");
        assertFalse(phones2.isEmpty());
        assertFalse(phones1.stream().findFirst().equals(phones2.stream().findFirst()));
    }
}
