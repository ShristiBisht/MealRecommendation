package com.meal.recommendation.meal_recommendation.service;

import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meal.recommendation.meal_recommendation.model.CuisineInfo;
import com.meal.recommendation.meal_recommendation.model.Restaurant;

@Service
public class MealRecommendationService {

    @Value("${documenu.api.key}")
    private String apiKey;

    private static final String DOCUMENU_URL = "https://documenu.p.rapidapi.com/restaurants/state/";
    private static final Logger logger = LogManager.getLogger("MealRecommendationService.class");

    public List<Restaurant> getRestaurantsFromDocumenu(CuisineInfo cuisineInfo) {
        String city=cuisineInfo.getCity();
        String cuisine = cuisineInfo.getCuisine();
        List<Restaurant> restaurants = new ArrayList<>();
        logger.info("Inside Service Class");

        try {
            // Create the HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Construct the URL with parameters
            String url = DOCUMENU_URL + city + "?size=10&cuisine=" + cuisine + "&top_cuisines=true&fullmenu=true&page=2";
            logger.info("Service Class URL : {}", url);

            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", "documenu.p.rapidapi.com")
                    .header("Accept", "application/json")
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("response: {}",response);

            // Log the response
            logger.info("Response Code: {}", response.statusCode());
            logger.info("Response Body: {}", response.body());

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode data = root.path("data");

            if (data.isArray()) {
                for (JsonNode node : data) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(node.path("restaurant_name").asText());
                    restaurant.setCity(node.path("address").path("city").asText());
                    restaurant.setCuisineType(cuisine); // From your form input
                    restaurants.add(restaurant);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}
