package com.Web.Online.Food.Ordering.controller;


import com.Web.Online.Food.Ordering.model.Food;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.CreateFoodRequest;
import com.Web.Online.Food.Ordering.response.Message;
import com.Web.Online.Food.Ordering.service.FoodService;
import com.Web.Online.Food.Ordering.service.RestaurantService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }


    // Admin Endpoints
    @PostMapping("/admin/food")
    public ResponseEntity<Food> createFood(
            @RequestBody CreateFoodRequest createFoodRequest,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(createFoodRequest.getRestaurantId());
        Food food = foodService.createFood(createFoodRequest, createFoodRequest.getCategory(), restaurant, user);

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(food);

    }

    // Admin Endpoints
    @DeleteMapping("/admin/food/{id}")
    public ResponseEntity<Message> deleteFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id, user);
        Message message = new Message("Food deleted successfully");
        return ResponseEntity.
                status(HttpStatus.OK).
                body(message);
    }

    // Admin Endpoints
    @PutMapping("/admin/food/{id}/availability")
    public ResponseEntity<Message> updateFoodAvailability(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.updateAvailibilityStatus(id, user);
        Message message = new Message(food.getName() +" availability change successfully");
        return ResponseEntity.
                status(HttpStatus.OK).
                body(message);
    }

    // Public/User Endpoints
    @GetMapping("/food/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestParam String name
    ) throws Exception {

        List<Food> food = foodService.searchFood(name);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(food);
    }

    // Public/User Endpoints
    @GetMapping("/food/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @PathVariable Long restaurantId,
            @RequestParam boolean vegetarian,
            @RequestParam boolean nonVeg,
            @RequestParam boolean seasonal,
            @RequestParam(required = false) String food_category
    ) throws Exception {

        List<Food> foods = foodService.getRestaurantFood(restaurantId, vegetarian, nonVeg, seasonal, food_category);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(foods);
    }

}
