package com.example.catalog_service.Controller;

import com.example.catalog_service.Exceptions.MenuItemEmptyException;
import com.example.catalog_service.Service.MenuItemService;
import com.example.catalog_service.dto.MenuItemRequestDTO;
import com.example.catalog_service.Exceptions.InvalidMenuItemCredentialsException;
import com.example.catalog_service.Exceptions.MenuItemAlreadyPresentException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.dto.MenuItemResponseDTO;
import com.example.catalog_service.enums.FoodCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
public class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMenuItem_Success() throws Exception {
        MenuItemRequestDTO menuItemRequestDTO = new MenuItemRequestDTO();
        menuItemRequestDTO.setName("Burger");
        menuItemRequestDTO.setPrice(10.0);

        doNothing().when(menuItemService).addMenuItem(any(MenuItemRequestDTO.class), anyLong());

        mockMvc.perform(post("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Burger\",\"price\":10.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Menu item added successfully"));
    }

    @Test
    void testAddMenuItem_InvalidCredentials() throws Exception {
        doThrow(new InvalidMenuItemCredentialsException("MenuItem Credentials are not empty"))
                .when(menuItemService).addMenuItem(any(MenuItemRequestDTO.class), anyLong());

        mockMvc.perform(post("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"price\":0.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MenuItem Credentials are not empty"));
    }

    @Test
    void testAddMenuItem_RestaurantNotFound() throws Exception {
        doThrow(new RestaurantNotFoundException("Restaurant not found"))
                .when(menuItemService).addMenuItem(any(MenuItemRequestDTO.class), anyLong());

        mockMvc.perform(post("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Burger\",\"price\":10.0}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Restaurant not found"));
    }

    @Test
    void testAddMenuItem_MenuItemAlreadyPresent() throws Exception {
        doThrow(new MenuItemAlreadyPresentException("Menu item already present"))
                .when(menuItemService).addMenuItem(any(MenuItemRequestDTO.class), anyLong());

        mockMvc.perform(post("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Burger\",\"price\":10.0}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Menu item already present"));
    }

    @Test
    void testGetMenuItems_Success() throws Exception {

        // Creating a mock menu item response
        List<MenuItemResponseDTO> menuItemList = Collections.singletonList(
                new MenuItemResponseDTO(1L, 1L, "Burger", FoodCategory.INDIAN, 10.0)
        );

        when(menuItemService.getMenuItems(anyLong())).thenReturn(menuItemList);

        String expectedJsonResponse = "[{\"id\":1,\"restaurantID\":1,\"name\":\"Burger\",\"category\":\"INDIAN\",\"price\":10.0}]";

        mockMvc.perform(get("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    void testGetMenuItems_Empty() throws Exception {
        doThrow(new MenuItemEmptyException("No menu items found"))
                .when(menuItemService).getMenuItems(anyLong());

        mockMvc.perform(get("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No menu items found"));
    }

    @Test
    void testGetMenuItems_InvalidRestaurantId() throws Exception {
        doThrow(new RestaurantNotFoundException("Restaurant not found"))
                .when(menuItemService).getMenuItems(anyLong());

        mockMvc.perform(get("/catalog/restaurant/1/menuItem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Restaurant not found"));
    }
}