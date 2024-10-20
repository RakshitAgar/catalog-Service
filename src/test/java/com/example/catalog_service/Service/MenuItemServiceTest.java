package com.example.catalog_service.Service;

import com.example.catalog_service.Exceptions.InvalidMenuItemCredentialsException;
import com.example.catalog_service.Exceptions.MenuItemEmptyException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.Model.MenuItem;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Repository.MenuItemRepository;
import com.example.catalog_service.Repository.RestaurantRepository;
import com.example.catalog_service.dto.MenuItemRequestDTO;
import com.example.catalog_service.dto.MenuItemResponseDTO;
import com.example.catalog_service.enums.FoodCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItemRequestDTO menuItemRequestDTO;
    private Restaurant restaurant;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItemRequestDTO = new MenuItemRequestDTO();
        menuItemRequestDTO.setName("Pizza");
        menuItemRequestDTO.setPrice(10.0);
        menuItemRequestDTO.setCategory(FoodCategory.CHINESE);

        restaurant = new Restaurant("Test Restaurant", "Test Location");
        menuItem = new MenuItem("Pizza", 10.0, FoodCategory.CHINESE, restaurant);
    }

    @Test
    void testGetMenuItems_EmptyList() {
        Long restaurantId = 1L;
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(MenuItemEmptyException.class, () -> menuItemService.getMenuItems(restaurantId));
    }

    @Test
    void testGetMenuItems_NonEmptyList() {
        Long restaurantId = 1L;
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem));

        List<MenuItemResponseDTO> menuItems = menuItemService.getMenuItems(restaurantId);

        assertEquals(1, menuItems.size());
        assertEquals("Pizza", menuItems.get(0).getName());
    }

    @Test
    void testAddMenuItem_InvalidCredentials() {
        menuItemRequestDTO.setName("");
        assertThrows(InvalidMenuItemCredentialsException.class, () -> menuItemService.addMenuItem(menuItemRequestDTO, 1L));
    }

    @Test
    void testAddMenuItem_RestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, () -> menuItemService.addMenuItem(menuItemRequestDTO, 1L));
    }

    @Test
    void testAddMenuItem_Success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        menuItemService.addMenuItem(menuItemRequestDTO, 1L);

        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }
}