package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.exceptions.OutOfStockException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Color;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context/applicationContext-core-test.xml")
@WebAppConfiguration
@Transactional
public class OrderServiceImplTest {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private OrderService orderService;
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
    void testCreateOrder() {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem(createdPhone, 1);
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        cart.setTotalQuantity(1);
        cart.setTotalCost(BigDecimal.valueOf(10));

        Order order = orderService.createOrder(cart);

        assertNotNull(order);
        assertEquals(1, order.getOrderItems().size());
        assertEquals(createdPhone.getId(), order.getOrderItems().get(0).getPhone().getId());
        assertEquals(cart.getTotalCost(), order.getSubtotal());
        assertEquals(5, order.getDeliveryPrice().intValue());
        assertEquals(15, order.getTotalPrice().intValue());
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getSecureId());
    }

    @Test
    void testPlaceOrder() throws OutOfStockException {
        Order order = createOrder(null, UUID.randomUUID().toString().replace("-", ""));

        orderService.placeOrder(order);

        assertNotNull(order.getId());
    }

    @Test
    void testGetOrderBySecureId() throws OutOfStockException {
        String secureId = UUID.randomUUID().toString().replace("-", "");
        Order order = createOrder(null, secureId);
        orderService.placeOrder(order);

        Order getOrder = orderService.getOrderBySecureId(secureId).get();

        assertNotNull(getOrder);
        assertEquals(order.getId(), getOrder.getId());
    }
}
