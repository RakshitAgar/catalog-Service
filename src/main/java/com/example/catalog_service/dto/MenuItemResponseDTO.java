package com.example.catalog_service.dto;

import com.example.catalog_service.enums.FoodCategory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuItemResponseDTO {
    private Long id;
    private Long restaurantID;
    private String name;
    private FoodCategory category;
    private Double price;

    public MenuItemResponseDTO(Long id, Long restaurantID, String name, FoodCategory category, Double price) {
        this.id = id;
        this.restaurantID = restaurantID;
        this.name = name;
        this.category = category;
        this.price = price;
    }

}
