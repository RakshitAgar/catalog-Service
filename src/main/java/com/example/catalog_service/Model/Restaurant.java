package com.example.catalog_service.Model;

import com.example.catalog_service.Exceptions.InvalidRestaurantRegistrationCredentials;
import com.example.catalog_service.Exceptions.MenuItemAlreadyPresentException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.CascadeType;

import java.util.ArrayList;
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
    @JsonIgnore
    private List<MenuItem> menuItems;

    public Restaurant(String name, String location) {
        if (name.isBlank() || name == null || location.isBlank() || location == null) {
            throw new InvalidRestaurantRegistrationCredentials("Name and location cannot be empty");
        }
        this.name = name;
        this.location = location;
        this.menuItems = new ArrayList<>();
    }

    public Restaurant() {
    }

    public void addMenuItem(MenuItem menuItem) {
        if (menuItems.stream().anyMatch(item -> item.getName().equals(menuItem.getName()))) {
            throw new MenuItemAlreadyPresentException("Menu item with the same name already exists");
        }
        menuItems.add(menuItem);
    }
}
