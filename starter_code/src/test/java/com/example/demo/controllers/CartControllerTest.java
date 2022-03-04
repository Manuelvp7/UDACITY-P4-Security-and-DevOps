package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CartControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    CartController cartController;

    @BeforeEach
    public void setUp(){
        initMocks(this);
    }

    @Test
    void testAddToCart() {

        Item itemMock = getItemMock();
        User userMock = createUserMock();
        ModifyCartRequest modifyCartRequestMock = getModifyCartRequestMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(userMock);
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(itemMock));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testAddToCartUserNotFound() {

        Item itemMock = getItemMock();
        ModifyCartRequest modifyCartRequestMock = getModifyCartRequestMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(null);
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(itemMock));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testAddToCartItemNotFound() {
        
        User userMock = createUserMock();
        ModifyCartRequest modifyCartRequestMock = getModifyCartRequestMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(userMock);
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testRemoveFromcart() {
        Item itemMock = getItemMock();
        User userMock = createUserMock();
        ModifyCartRequest modifyCartRequestMock = getModifyCartRequestMock();

        when(userRepository.findByUsername(eq("MANOLO"))).thenReturn(userMock);
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(itemMock));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequestMock);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private ModifyCartRequest getModifyCartRequestMock() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("MANOLO");
        modifyCartRequest.setQuantity(1);

        return modifyCartRequest;
    }

    private User createUserMock(){
        User user = new User();
        user.setUsername("MANOLO");
        user.setPassword("1234567");
        user.setCart(new Cart());

        return user;

    }

    private Item getItemMock(){
        Item item = new Item();
        item.setDescription("Description");
        item.setId(1L);
        item.setName("Name");
        item.setPrice(BigDecimal.valueOf(10));

        return item;
    }
}