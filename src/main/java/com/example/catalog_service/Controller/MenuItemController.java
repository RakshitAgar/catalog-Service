package com.example.catalog_service.Controller;

import com.example.catalog_service.Exceptions.InvalidMenuItemCredentialsException;
import com.example.catalog_service.Exceptions.MenuItemAlreadyPresentException;
import com.example.catalog_service.Exceptions.MenuItemEmptyException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.Model.MenuItem;
import com.example.catalog_service.Service.MenuItemService;
import com.example.catalog_service.Service.RestaurantService;
import com.example.catalog_service.dto.MenuItemRequestDTO;
import com.example.catalog_service.dto.MenuItemResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/restaurant/{restaurantId}")
@CrossOrigin
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/menuItem")
    public ResponseEntity<?> getMenuItems(@PathVariable Long restaurantId) {
        try {
            List<MenuItemResponseDTO> menuItemList = menuItemService.getMenuItems(restaurantId);
            return ResponseEntity.ok(menuItemList);
        } catch (MenuItemEmptyException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/menuItem")
    public ResponseEntity<?> addMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItemRequestDTO menuItemRequestDTO) {
        try {
            menuItemService.addMenuItem(menuItemRequestDTO,restaurantId);
            return ResponseEntity.ok("Menu item added successfully");
        } catch (InvalidMenuItemCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MenuItemAlreadyPresentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/menu/{menuItemId}")
    public ResponseEntity<?> getMenuItemById(@PathVariable Long restaurantId, @PathVariable Long menuItemId) {
        try {
            MenuItemResponseDTO menuItem = menuItemService.getMenuItemById(restaurantId, menuItemId);
            return ResponseEntity.ok(menuItem);
        } catch (RestaurantNotFoundException | MenuItemEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}

/*
BASIC API
-GET /catalog/restaurant/{restaurantId}/menu. -> get all menu item for the restaurant
-GET /catalog/restaurant/{restaurantId}/menu/{menuItemId}. -> get menu item by id
-POST /catalog/restaurant/{restaurantId}/menu. -> add menu item
 */
