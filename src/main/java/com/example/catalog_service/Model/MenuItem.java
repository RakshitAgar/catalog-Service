package com.example.catalog_service.Model;

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
    private double price;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
    }

    public MenuItem() {
    }
}
