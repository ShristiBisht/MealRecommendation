package com.meal.recommendation.meal_recommendation.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meal.recommendation.meal_recommendation.config.KafkaProducerConfig;
import com.meal.recommendation.meal_recommendation.model.CuisineInfo;
import com.meal.recommendation.meal_recommendation.model.Restaurant;
import com.meal.recommendation.meal_recommendation.service.CacheService;
import com.meal.recommendation.meal_recommendation.service.MealRecommendationService;

@Controller
public class MealRecommendationController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private CacheService cacheService;
    @Autowired
    private MealRecommendationService mealRecommendationService;
    @Autowired
    KafkaProducerConfig kafkaProducerConfig;
    @Value("${kafka.topic.name}")
    String kafkaTopic;

    CuisineInfo cuisineInfo= new CuisineInfo();
    

    private static final Logger logger = LogManager.getLogger(MealRecommendationController.class);


    // Handle form submission
    @GetMapping("/submitForm")
    public String submitForm(@RequestParam String cuisine, @RequestParam String city, @RequestParam String requestId) {
        logger.info("Request id : {}",requestId);
        // Send to Kafka
        String message = String.format("{\"requestId\":\"%s\", \"cuisine\":\"%s\", \"city\":\"%s\"}", requestId, cuisine, city);

        kafkaTemplate.send(kafkaTopic, message);
        // Save the initial mapping (empty results for now)
        cuisineInfo.setCuisine(cuisine);
        cuisineInfo.setCity(city);
        cacheService.saveResultsToCache( cuisineInfo, List.of());

        return "redirect:/getResult?requestId=" + requestId;
    }

    // Handle real-time result
    @MessageMapping("/getResult/{requestId}")
    public void getResult(@Payload CuisineInfo request,@DestinationVariable String requestId) {
        String city = request.getCity();
        String cuisine = request.getCuisine();
        // List<Restaurant> results = cacheService.fetchResultsFromCache(requestId);
        List<Restaurant> results = mealRecommendationService.getRestaurantsFromDocumenu(cuisine,city);

        messagingTemplate.convertAndSend("/topic/meal-results/" + requestId, results);
    }
}

