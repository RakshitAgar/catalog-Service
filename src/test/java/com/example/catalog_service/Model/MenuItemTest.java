package com.example.catalog_service.Model;

import com.example.catalog_service.enums.FoodCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    public void testMenuItemConstructor() {
        assertDoesNotThrow(() -> new MenuItem("Burger", 10.0, FoodCategory.CHINESE, new Restaurant()));
    }

}