package com.Web.Online.Food.Ordering.controller;


import com.Web.Online.Food.Ordering.model.Category;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.service.CategoryService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(
            @RequestHeader("Authentication") String jwt,
            @RequestBody String categoryName
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Category category = categoryService.createCategory(categoryName, user.getId());
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(category);
    }

    @GetMapping("/category/restaurant/{id}")
    public ResponseEntity<List<Category>> getRestaurantCategory(
            @PathVariable Long restaurantId
    ) throws Exception {
        List<Category> category = categoryService.findCategoryByRestaurantId(restaurantId);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(category);
    }
}
