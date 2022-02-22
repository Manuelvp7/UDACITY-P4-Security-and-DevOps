package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class OrderControllerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp(){
        initMocks(this);
    }

    @Test
    void testSubmitOrder() {
        User userMock = createUserMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(userMock);
        ResponseEntity<UserOrder> response = orderController.submit("MANOLO");
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testSubmitOrderUserNotFound() {

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("MANOLO");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testgetOrdersForUser() {
        User userMock = createUserMock();
        List<UserOrder> userOrders = userOrdersMock();


        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(userMock);
        when(orderRepository.findByUser(eq(userMock))).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("MANOLO");
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testgetOrdersForUserNotFound() {
        User userMock = createUserMock();
        List<UserOrder> userOrders = userOrdersMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("MANOLO");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    private List<UserOrder> userOrdersMock(){
        User user = createUserMock();
        List<Item> itemList = getItemsListMock();

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(itemList);
        userOrder.setId(1L);
        userOrder.setTotal(BigDecimal.valueOf(10));

        userOrders.add(userOrder);
        return userOrders;
    }

    private User createUserMock(){

        List<Item> itemsList = getItemsListMock();

        Cart cart = new Cart();
        cart.setItems(itemsList);

        User user = new User();
        user.setUsername("MANOLO");
        user.setPassword("1234567");
        user.setCart(cart);

        return user;

    }

    private List<Item> getItemsListMock(){
        List<Item> itemsList = new ArrayList<>();
        Item item = new Item();
        item.setDescription("Description");
        item.setId(1L);
        item.setName("Name");
        item.setPrice(BigDecimal.valueOf(10));
        itemsList.add(item);

        return itemsList;
    }


}