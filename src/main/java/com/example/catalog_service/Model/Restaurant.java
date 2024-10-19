package com.example.catalog_service.Model;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.CascadeType;

import java.util.List;

@Data
@Entity
@Getter
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    public Restaurant(String name, String location) {
        if(name.isBlank() || name == null || location.isBlank() || location == null){
            throw new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty");
        }
        this.name = name;
        this.location = location;
    }

    public Restaurant() {
    }
}
