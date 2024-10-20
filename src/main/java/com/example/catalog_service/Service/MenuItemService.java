package com.example.catalog_service.Service;

import com.example.catalog_service.Exceptions.InvalidMenuItemCredentialsException;
import com.example.catalog_service.Exceptions.MenuItemAlreadyPresentException;
import com.example.catalog_service.Exceptions.MenuItemEmptyException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.Model.MenuItem;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Repository.MenuItemRepository;
import com.example.catalog_service.Repository.RestaurantRepository;
import com.example.catalog_service.dto.MenuItemRequestDTO;
import com.example.catalog_service.dto.MenuItemResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    public List<MenuItemResponseDTO> getMenuItems(Long restaurantId) {

        try {
            if(!restaurantRepository.existsById(restaurantId)){
                throw new RestaurantNotFoundException("Restaurant not found");
            }
            List<MenuItem> menuItems = menuItemRepository.findAll();
            if (menuItems.isEmpty()) {
                throw new MenuItemEmptyException("No menu items found");
            }
            return menuItems.stream()
                    .map(menuItem -> new MenuItemResponseDTO(
                            menuItem.getId(),
                            menuItem.getRestaurant().getId(),
                            menuItem.getName(),
                            menuItem.getCategory(),
                            menuItem.getPrice()))
                    .collect(Collectors.toList());
        } catch (MenuItemEmptyException e) {
            throw new MenuItemEmptyException(e.getMessage());
        } catch (RestaurantNotFoundException e) {
            throw new RestaurantNotFoundException(e.getMessage());
        }
    }

public void addMenuItem(MenuItemRequestDTO menuItemRequestDTO, Long restaurantId) {
    try {
        if (menuItemRequestDTO.getName().isEmpty() || menuItemRequestDTO.getPrice().equals(0.0) || menuItemRequestDTO.getCategory() == null) {
            throw new InvalidMenuItemCredentialsException("MenuItem Credentials are not empty");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        MenuItem menuItem = new MenuItem(menuItemRequestDTO.getName(), menuItemRequestDTO.getPrice(), menuItemRequestDTO.getCategory(), restaurant);

        restaurant.addMenuItem(menuItem);
        menuItemRepository.save(menuItem);
    } catch (InvalidMenuItemCredentialsException e) {
        throw new InvalidMenuItemCredentialsException(e.getMessage());
    } catch (RestaurantNotFoundException e) {
        throw new RestaurantNotFoundException(e.getMessage());
    } catch (MenuItemAlreadyPresentException e) {
        throw new MenuItemAlreadyPresentException(e.getMessage());
    }
}

    public MenuItemResponseDTO getMenuItemById(Long restaurantId, Long menuItemId) {
        try {
            if (!restaurantRepository.existsById(restaurantId)) {
                throw new RestaurantNotFoundException("Restaurant not found");
            }

            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new MenuItemEmptyException("Menu item not found"));

            return new MenuItemResponseDTO(
                    menuItem.getId(),
                    menuItem.getRestaurant().getId(),
                    menuItem.getName(),
                    menuItem.getCategory(),
                    menuItem.getPrice()
            );
        } catch (RestaurantNotFoundException e) {
            throw new RestaurantNotFoundException(e.getMessage());
        } catch (MenuItemEmptyException e) {
            throw new MenuItemEmptyException(e.getMessage());
        }
    }
}
