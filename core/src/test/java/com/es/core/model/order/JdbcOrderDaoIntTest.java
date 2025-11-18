package com.es.core.model.order;

import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
@Transactional
public class JdbcOrderDaoIntTest {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private OrderDao orderDao;
    private Phone createdPhone;
    private Color createdColor;

    @BeforeEach()
    void setUpData() {
        createdPhone = new Phone();
        createdColor = new Color();
        setCreatedColor();
        setCreatedPhone();
    }

    Order createOrder(Long id, String secureId) {
        Order createdOrder = new Order();
        createdOrder.setId(id);
        createdOrder.setSecureId(secureId);
        createdOrder.setSubtotal(BigDecimal.valueOf(10));
        createdOrder.setDeliveryPrice(BigDecimal.ONE);
        createdOrder.setTotalPrice(BigDecimal.valueOf(11));
        createdOrder.setFirstName("John");
        createdOrder.setLastName("Smith");
        createdOrder.setDeliveryAddress("Address");
        createdOrder.setContactPhoneNo("+375333333333");
        createdOrder.setAdditionalInfo("AdditionalInfo");
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder.setStatus(OrderStatus.NEW);

        List<OrderItem> createdOrderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setOrder(createdOrder);
        orderItem.setId(1L);
        orderItem.setPhone(createdPhone);

        createdOrderItems.add(orderItem);
        createdOrder.setOrderItems(createdOrderItems);

        return createdOrder;
    }

    void setCreatedPhone() {
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

        HashSet<Color> colors = new HashSet<>();
        colors.add(createdColor);

        createdPhone.setColors(colors);
        phoneDao.save(createdPhone);
    }

    void setCreatedColor() {
        createdColor.setId(1013L);
        createdColor.setCode("TEST");
    }

    @Test
    void testSaveOrderWithItems(){
        Order order = createOrder(null, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(order);
        Optional<Order> getOrder = orderDao.getBySecureId(order.getSecureId());

        assertNotNull(getOrder);
        assertEquals(order.getId(), getOrder.get().getId());
        assertEquals(order.getSecureId(), getOrder.get().getSecureId());
        assertEquals(order.getStatus(), getOrder.get().getStatus());
        assertEquals(order.getDeliveryAddress(), getOrder.get().getDeliveryAddress());
        assertEquals(order.getContactPhoneNo(), getOrder.get().getContactPhoneNo());
    }

    @Test
    void testSaveExistingOrderWithItems(){
        Order createdOrder = createOrder(null, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        Optional<Order> order = orderDao.getBySecureId(createdOrder.getSecureId());

        assertNotNull(order);

        orderDao.saveOrderWithItems(order.get());

        order = orderDao.getBySecureId(createdOrder.getSecureId());

        assertNotNull(order);
        assertEquals(createdOrder.getId(), order.get().getId());
        assertEquals(createdOrder.getSecureId(), order.get().getSecureId());
        assertEquals(createdOrder.getStatus(), order.get().getStatus());
        assertEquals(createdOrder.getDeliveryAddress(), order.get().getDeliveryAddress());
        assertEquals(createdOrder.getContactPhoneNo(), order.get().getContactPhoneNo());
    }

    @Test
    void testSaveNonExistingOrderWithItemsWithId(){
        Order createdOrder = createOrder(2L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        Optional<Order> order = orderDao.getBySecureId(createdOrder.getSecureId());

        assertNotNull(order);
        assertEquals(createdOrder.getId(), order.get().getId());
        assertEquals(createdOrder.getSecureId(), order.get().getSecureId());
        assertEquals(createdOrder.getStatus(), order.get().getStatus());
        assertEquals(createdOrder.getDeliveryAddress(), order.get().getDeliveryAddress());
        assertEquals(createdOrder.getContactPhoneNo(), order.get().getContactPhoneNo());
    }

    @Test
    void testFindAll(){
        Order createdOrder = createOrder(20L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(40L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(50L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);

        List<Order> orders = orderDao.findAll();

        assertNotNull(orders);
        assertEquals(3, orders.size());
    }

    @Test
    void testGetById(){
        Order createdOrder = createOrder(3L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(4L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(5L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);

        Order order = orderDao.getById(createdOrder.getId()).get();

        assertNotNull(order);
        assertEquals(createdOrder.getSecureId(), order.getSecureId());
    }

    @Test
    void testUpdateOrderStatus(){
        Order createdOrder = createOrder(3L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(4L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);
        createdOrder = createOrder(5L, UUID.randomUUID().toString().replace("-", ""));
        orderDao.saveOrderWithItems(createdOrder);

        createdOrder.setStatus(OrderStatus.DELIVERED);
        orderDao.updateOrderStatus(createdOrder);

        Order order = orderDao.getById(createdOrder.getId()).get();

        assertNotNull(order);
        assertEquals(createdOrder.getSecureId(), order.getSecureId());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void testUpdateOrderStatusException(){
        Order createdOrder = createOrder(3L, UUID.randomUUID().toString().replace("-", ""));
        createdOrder.setStatus(OrderStatus.DELIVERED);
        assertThrows(DatabaseUpdateException.class, () -> orderDao.updateOrderStatus(createdOrder));
    }
}
