package com.example.catalog_service.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.CascadeType;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Entity
@Getter
@Table(name = "restaurants")
public class Restaurant {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Restaurant() {
    }
}
