package com.meal.recommendation.meal_recommendation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meal.recommendation.meal_recommendation.model.CuisineInfo;
import com.meal.recommendation.meal_recommendation.model.Restaurant;

@Service
public class CacheService {

    private Map<String, List<Restaurant>> cache = new HashMap<>();
   

    public List<Restaurant> fetchResultsFromCache( CuisineInfo cuisineInfo) {
        String key = generateCacheKey(cuisineInfo.getCuisine(), cuisineInfo.getCity());
        return cache.get(key);
    }

    public void saveResultsToCache(CuisineInfo cuisineInfo, List<Restaurant> restaurants) {
        String key = generateCacheKey(cuisineInfo.getCuisine(), cuisineInfo.getCity());
        cache.put(key, restaurants);
    }

    private String generateCacheKey(String cuisine, String city) {
        return cuisine.toLowerCase() + "-" + city.toLowerCase();
    }
    public boolean isPresentInCache(CuisineInfo cuisineInfo) {
        String key = generateCacheKey(cuisineInfo.getCuisine(), cuisineInfo.getCity());
        return cache.containsKey(key);
    }
    public void updateResultsInCache(CuisineInfo cuisineInfo, List<Restaurant> restaurants) {
        String key = generateCacheKey(cuisineInfo.getCuisine(), cuisineInfo.getCity());
        cache.put(key, restaurants);
    }
}
