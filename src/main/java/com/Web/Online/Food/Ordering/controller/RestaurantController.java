package com.Web.Online.Food.Ordering.controller;

import com.Web.Online.Food.Ordering.dto.RestaurantDto;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.CreateRestaurantRequest;
import com.Web.Online.Food.Ordering.request.UpdateRestaurantRequest;
import com.Web.Online.Food.Ordering.service.RestaurantService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;

    public RestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    // Admin Endpoints
    @PostMapping("/admin/restaurant")
    public ResponseEntity<?> createRestaurant(
            @RequestBody CreateRestaurantRequest createRestaurantRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(createRestaurantRequest, user);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(restaurant.getName() + "Restaurant successfully created");
    }

    // Admin Endpoints
    @PutMapping("/admin/restaurant/{id}")
    public ResponseEntity<?> updateRestaurant(
            @RequestBody UpdateRestaurantRequest updateRestaurantRequest,
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurant(id,updateRestaurantRequest, user);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(updateRestaurantRequest);
    }

    // Admin Endpoints
    @DeleteMapping("/admin/restaurant/{id}")
    public ResponseEntity<?> deleteRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        String restaurantName = restaurantService.deleteRestaurant(id, user);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurantName + "successfully deleted");
    }

    // Admin Endpoints
    @PutMapping("/admin/restaurant/{id}/status")
    public ResponseEntity<?> updateRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id, user);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurant.getName() + "Restaurant open status change successfully ");
    }

    // Admin Endpoints
    @GetMapping("/admin/restaurant/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());

        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurant);
    }

    // Public/User Endpoints
    @GetMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
    ) throws Exception {

        List<Restaurant> restaurant  = restaurantService.getAllRestaurants();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurant);
    }

    // Public/User Endpoints
    @GetMapping("/restaurant/user")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestParam String keyword
    ) throws Exception {

        List<Restaurant> restaurant  = restaurantService.searchRestaurant(keyword);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurant);
    }

    // Public/User Endpoints
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @PathVariable Long id
            ) throws Exception {

        Restaurant restaurant  = restaurantService.findRestaurantById(id);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurant);
    }

    // Public/User Endpoints
    @PutMapping("/restaurant/{id}/add-favorites")
    public ResponseEntity<RestaurantDto> addToFavorites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        RestaurantDto restaurantDto = restaurantService.addToFavorites(id, user);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(restaurantDto);
    }

}
