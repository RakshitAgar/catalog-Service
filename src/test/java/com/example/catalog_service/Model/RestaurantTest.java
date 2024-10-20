package com.example.catalog_service.Model;

import com.example.catalog_service.Exceptions.InvalidMenuItemCredentialsException;
import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.MenuItemAlreadyPresentException;
import com.example.catalog_service.enums.FoodCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    public void testRestaurant() {
        Restaurant restaurant = new Restaurant("Test Restaurant", "Test Location");
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("Test Location", restaurant.getLocation());
    }

    @Test
    public void testRestaurantExceptionNameIsEmpty() {
        assertThrows(InvalidRestaurantRegistrationCredentials.class, () -> {
             new Restaurant("", "location");
        });
    }

    @Test
    public void testRestaurantExceptionLocationIsEmpty() {
        assertThrows(InvalidRestaurantRegistrationCredentials.class, () -> {
            new Restaurant("Name", "");
        });
    }

    @Test
    public void testAddMenuItem() {
        Restaurant restaurant = new Restaurant("Test Restaurant", "Test Location");
        MenuItem menuItem = new MenuItem("Pizza", 10.0, FoodCategory.CHINESE, restaurant);
        restaurant.addMenuItem(menuItem);
        assertEquals(1, restaurant.getMenuItems().size());
    }

    @Test
    void testAddMenuItem_MenuItemAlreadyPresent() {
        Restaurant restaurant = new Restaurant("Test Restaurant", "Test Location");
        MenuItem menuItem = new MenuItem("Burger", 5.99, FoodCategory.CHINESE, restaurant);
        restaurant.addMenuItem(menuItem);

        MenuItemAlreadyPresentException exception = assertThrows(MenuItemAlreadyPresentException.class, () -> {
            restaurant.addMenuItem(new MenuItem("Burger", 6.99, FoodCategory.CHINESE, restaurant));
        });

        assertEquals("Menu item with the same name already exists", exception.getMessage());
    }
}