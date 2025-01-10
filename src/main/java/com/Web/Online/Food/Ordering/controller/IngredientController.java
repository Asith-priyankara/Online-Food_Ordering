package com.Web.Online.Food.Ordering.controller;


import com.Web.Online.Food.Ordering.model.IngredientCategory;
import com.Web.Online.Food.Ordering.model.IngredientsItem;
import com.Web.Online.Food.Ordering.request.CreateIngredientCategoryRequest;
import com.Web.Online.Food.Ordering.request.CreateIngredientItemRequest;
import com.Web.Online.Food.Ordering.response.Message;
import com.Web.Online.Food.Ordering.service.IngredientService;
import com.Web.Online.Food.Ordering.service.RestaurantService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IngredientController {

    private final IngredientService ingredientService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public IngredientController(IngredientService ingredientService, UserService userService, RestaurantService restaurantService) {
        this.ingredientService = ingredientService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @PostMapping("")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody CreateIngredientCategoryRequest createIngredientCategoryRequest,
            @RequestHeader("Authentication") String jwt
    ) throws Exception {
        IngredientCategory ingredientCategory = ingredientService.createIngredientCategory(createIngredientCategoryRequest.getCategoryName(), createIngredientCategoryRequest.getRestaurantId());
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(ingredientCategory);
    }

    @PostMapping("")
    public ResponseEntity<IngredientsItem> createIngredientItem(
            @RequestBody CreateIngredientItemRequest createIngredientItemRequest,
            @RequestHeader("Authentication") String jwt
    ) throws Exception {
        IngredientsItem ingredientsItem = ingredientService.createIngredientsItem(createIngredientItemRequest.getRestaurantId(), createIngredientItemRequest.getIngredientName(), createIngredientItemRequest.getCategoryId());
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(ingredientsItem);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Message> updateIngredientStock(
            @RequestHeader("Authentication") String jwt,
            @PathVariable Long id
    ) throws Exception {
        IngredientsItem ingredientsItem = ingredientService.updateStock(id);
        Message message = new Message(ingredientsItem.getName() + "Stock updated");
        return ResponseEntity.
                status(HttpStatus.OK).
                body(message);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredients(
            @PathVariable Long id
    ) throws Exception {
        List<IngredientsItem> ingredientsItems = ingredientService.findRestaurantIngredients(id);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(ingredientsItems);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientsCategory(
            @PathVariable Long id
    ) throws Exception {
        List<IngredientCategory> ingredientCategories = ingredientService.findIngredientCategoryByRestaurantId(id);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(ingredientCategories);
    }
}
