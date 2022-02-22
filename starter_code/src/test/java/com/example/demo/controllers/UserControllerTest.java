package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest {


    @InjectMocks
    UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp(){
        initMocks(this);
    }

    @Test
    void testUserCreation() throws Exception {

        User userMock = createUserMock();
        CreateUserRequest createUserRequestMock = createUserRequestMock();

        when(userRepository.save(eq(userMock))).thenReturn(userMock);
        when(bCryptPasswordEncoder.encode(eq(userMock.getPassword()))).thenReturn(userMock.getPassword());
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());
        ResponseEntity<User> response =  userController.createUser(createUserRequestMock);

        verify(userRepository, times(1)).save(eq(userMock));
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(bCryptPasswordEncoder, times(1)).encode(eq(userMock.getPassword()));
        
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testUserCreationWithoutValidPassword() throws Exception {

        User userMock = createUserMock();
        CreateUserRequest createUserRequestMock = createUserRequestMock();
        createUserRequestMock.setConfirmPassword("12345");
        createUserRequestMock.setPassword("12345");

        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());
        ResponseEntity<User> response =  userController.createUser(createUserRequestMock);

        verify(userRepository, times(0)).save(any(User.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(bCryptPasswordEncoder, times(0)).encode(anyString());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void testFindUserById() {
        User userMock = createUserMock();

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(userMock));
        ResponseEntity<User> response =  userController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindByUsername() {
        User userMock = createUserMock();

        when(userRepository.findByUsername(eq("TestUser"))).thenReturn(userMock);
        ResponseEntity<User> response =  userController.findByUserName("TestUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private User createUserMock(){
        User user = new User();
        user.setUsername("MANOLO");
        user.setPassword("1234567");
        user.setCart(new Cart());

        return user;

    }
    private CreateUserRequest createUserRequestMock(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("MANOLO");
        userRequest.setPassword("1234567");
        userRequest.setConfirmPassword("1234567");

        return userRequest;
    }
}