package com.example.catalog_service.Controller;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.RestaurantAlreadyExistException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Service.RestaurantService;
import com.example.catalog_service.dto.RestaurantRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@CrossOrigin
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;


    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurants() {
        List<Restaurant> restaurantList = restaurantService.getRestaurants();
        return ResponseEntity.ok(restaurantList);
    }

    @PostMapping("/restaurants")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantRequestDTO restaurantRequestDTO) {
        try {

            restaurantService.addRestaurant(restaurantRequestDTO.getName(), restaurantRequestDTO.getLocation());
            return ResponseEntity.ok("Restaurant added successfully");
        } catch (InvalidRestaurantRegistrationCredentials e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RestaurantAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long restaurantId) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
            return ResponseEntity.ok(restaurant);
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}


/*
BASIC API ENDPOINTS
-GET /catalog/restaurants
-POST /catalog/restaurants
-GET /catalog/restaurants/{restaurantId}
 */