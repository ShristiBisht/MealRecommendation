// package com.meal.recommendation.meal_recommendation.controller;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// @Controller
// public class RecommendationController {
//     private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

//     @PostMapping("/submit")
//     public String handleFormSubmit(@RequestParam("cuisine") String cuisine, 
//                                    @RequestParam("city") String city, 
//                                    Model model) {
//         logger.info("We will render submit");
//         // Process the input data (you can call your service here)
//         model.addAttribute("cuisine", cuisine);
//         model.addAttribute("city", city);
        
//         // For now, let's return a simple response page with submitted details
//         return "result.html";  // Will map to /src/main/resources/templates/result.html
//     }

//     @RequestMapping("/")
//     public String home() {
//         logger.info("We will render index");
//         return "index.html";  // Renders /src/main/resources/static/index.html
//     }
// }
