package com.example.catalog_service.Model;

import com.example.catalog_service.enums.FoodCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "menu_items")
@Getter
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String name;
    @Enumerated(EnumType.STRING)
    private FoodCategory category;
    private Double price;

    public MenuItem(String name, Double price, FoodCategory category, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
    }

    public MenuItem() {
    }
}
