package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    ItemController itemController;

    @BeforeEach
    public void setUp(){
        initMocks(this);
    }

    @Test
    void getItemsTest() {
        List<Item> itemList = getItemsListMock();
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void getItemById () {
        Item itemMock = getItemMock();
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(itemMock));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItemByName() {
        List<Item> itemList = getItemsListMock();
        when(itemRepository.findByName(eq("Name"))).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
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

    private Item getItemMock(){
        Item item = new Item();
        item.setDescription("Description");
        item.setId(1L);
        item.setName("Name");
        item.setPrice(BigDecimal.valueOf(10));

        return item;
    }
}