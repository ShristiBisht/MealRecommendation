package com.meal.recommendation.meal_recommendation.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private String name;
    private String city;
    private String cuisineType;
    private List<String> healthyRecipes; // List of healthy recipes

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", healthyRecipes=" + healthyRecipes +
                '}';
    }
}
