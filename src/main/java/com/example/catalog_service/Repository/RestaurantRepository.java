package com.example.catalog_service.Repository;

import com.example.catalog_service.Model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    Optional<Restaurant> findByNameAndLocation(String name, String location);
}
