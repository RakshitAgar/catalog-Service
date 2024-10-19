package com.example.catalog_service.Service;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.RestaurantAlreadyExistException;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRestaurants() {
        Restaurant restaurant = new Restaurant("Test Restaurant", "Test Location");
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<Restaurant> restaurants = restaurantService.getRestaurants();

        assertNotNull(restaurants);
        assertEquals(1, restaurants.size());
        assertEquals("Test Restaurant", restaurants.get(0).getName());
    }

    @Test
    void testGetRestaurants_NoRestaurantsFound() {
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.getRestaurants();
        });

        assertEquals("No restaurants found", exception.getMessage());
    }

    @Test
    void testAddRestaurant_Success() {
        String name = "New Restaurant";
        String location = "New Location";

        when(restaurantRepository.findByNameAndLocation(name, location)).thenReturn(Optional.empty());

        restaurantService.addRestaurant(name, location);

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurant_InvalidCredentials() {
        InvalidRestaurantRegistrationCredentials exception = assertThrows(InvalidRestaurantRegistrationCredentials.class, () -> {
            restaurantService.addRestaurant(null, "Location");
        });

        assertEquals("Name and location cannot be empty", exception.getMessage());
    }

    @Test
    void testAddRestaurant_RestaurantAlreadyExists() {
        String name = "Existing Restaurant";
        String location = "Existing Location";

        when(restaurantRepository.findByNameAndLocation(name, location)).thenReturn(Optional.of(new Restaurant(name, location)));

        RestaurantAlreadyExistException exception = assertThrows(RestaurantAlreadyExistException.class, () -> {
            restaurantService.addRestaurant(name, location);
        });

        assertEquals("Restaurant with same name and location alreadyExists", exception.getMessage());
    }
}