package com.example.catalog_service.dto;

import com.example.catalog_service.enums.FoodCategory;
import jdk.jfr.Category;
import lombok.Data;

@Data
public class MenuItemRequestDTO {
    private String name;
    private Double price;
    private FoodCategory category;
}
