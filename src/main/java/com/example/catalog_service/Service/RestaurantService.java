package com.example.catalog_service.Service;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.RestaurantAlreadyExistException;
import com.example.catalog_service.Model.Restaurant;
import com.example.catalog_service.Repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private RestaurantRepository restaurantRepository;


    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public void addRestaurant(String name, String Location) {
        if(name == null || Location == null){
            throw new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty");
        }
        // finding that the restaurant with same name and location cannot Exist
        if(restaurantRepository.findByName(name).isPresent() && restaurantRepository.findByLocation(Location).isPresent()){
            throw new RestaurantAlreadyExistException("Restaurant with same name and location alreadyExists");
        }

        restaurantRepository.save(new Restaurant(name,Location));
    }
}
