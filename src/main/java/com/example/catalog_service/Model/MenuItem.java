package com.example.catalog_service.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Data
@Entity
@Table(name = "menu_items")
@Getter
public class MenuItem {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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
