package com.meal.recommendation.meal_recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuisineInfo {
    private String city;
    private String cuisine;
    private String requestID;
}
