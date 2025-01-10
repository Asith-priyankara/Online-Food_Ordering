package com.Web.Online.Food.Ordering.service;


import com.Web.Online.Food.Ordering.model.Category;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;

    public CategoryService(CategoryRepository categoryRepository, RestaurantService restaurantService) {
        this.categoryRepository = categoryRepository;
        this.restaurantService = restaurantService;
    }


    public Category createCategory(String categoryName, Long userId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        Category category = new Category();
        category.setName(categoryName);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception{
        return  categoryRepository.findByRestaurantId(restaurantId);
    }

    public Category findeCategoryById(Long id) throws Exception{
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category not found");
        }
        return category.get();
    }
}
