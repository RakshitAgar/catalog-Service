package com.example.catalog_service.Service;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.RestaurantAlreadyExistException;
import com.example.catalog_service.Exceptions.RestaurantNotFoundException;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;


    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        if(restaurantList.isEmpty()){
            throw new RuntimeException("No restaurants found");
        }
        return restaurantList;
    }

    public void addRestaurant(String name, String Location) {
        if(name == null || Location == null){
            throw new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty");
        }
        // finding that the restaurant with same name and location cannot Exist
        if(restaurantRepository.findByNameAndLocation(name, Location).isPresent()){
            throw new RestaurantAlreadyExistException("Restaurant with same name and location alreadyExists");
        }

        restaurantRepository.save(new Restaurant(name,Location));
    }

    public Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
    }
}
