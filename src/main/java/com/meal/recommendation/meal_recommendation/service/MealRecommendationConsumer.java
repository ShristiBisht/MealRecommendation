package com.meal.recommendation.meal_recommendation.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meal.recommendation.meal_recommendation.model.Restaurant;
import com.meal.recommendation.meal_recommendation.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealRecommendationConsumer {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "meal-responses", groupId = "meal-group")
    public void consume(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);

            String requestId = rootNode.get("requestId").asText();

            List<Restaurant> restaurants = new ArrayList<>();
            JsonNode restaurantArray = rootNode.get("restaurants");

            for (JsonNode node : restaurantArray) {
                String name = node.get("name").asText();
                String address = node.get("address").asText();
                Restaurant restaurant = new Restaurant(name, address);
                restaurants.add(restaurant);
            }

            // Update the cache
            cacheService.updateResultsInCache(requestId, restaurants);

            // Notify frontend immediately (real-time push)
            messagingTemplate.convertAndSend("/topic/meal-results/" + requestId, restaurants);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
