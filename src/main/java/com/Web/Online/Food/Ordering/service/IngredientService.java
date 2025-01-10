package com.Web.Online.Food.Ordering.service;


import com.Web.Online.Food.Ordering.model.Category;
import com.Web.Online.Food.Ordering.model.IngredientCategory;
import com.Web.Online.Food.Ordering.model.IngredientsItem;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.repository.IngredientCategoryRepository;
import com.Web.Online.Food.Ordering.repository.IngredientItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientItemRepository ingredientItemRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RestaurantService restaurantService;

    public IngredientService(IngredientItemRepository ingredientItemRepository, IngredientCategoryRepository ingredientCategoryRepository, RestaurantService restaurantService) {
        this.ingredientItemRepository = ingredientItemRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.restaurantService = restaurantService;
    }

    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setName(name);
        ingredientCategory.setRestaurant(restaurant);
        return ingredientCategoryRepository.save(ingredientCategory);
    }

    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(id);
        if (ingredientCategory.isEmpty()) {
            throw new Exception("Ingredient category not found");
        }
        return ingredientCategory.get();
    }

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception {
        restaurantService.findRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = ingredientCategoryRepository.findById(categoryId).get();
        IngredientsItem ingredientsItem = new IngredientsItem();
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setCategory(category);
        ingredientsItem.setName(ingredientName);

        IngredientsItem ingredientsItemSaved = ingredientItemRepository.save(ingredientsItem);
        category.getIngredients().add(ingredientsItemSaved);

        return ingredientsItemSaved;
    }

    public List<IngredientsItem> findRestaurantIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    public IngredientsItem updateStock (Long ingredientId) throws Exception {
        Optional<IngredientsItem> ingredientsItem = ingredientItemRepository.findById(ingredientId);
        if (ingredientsItem.isEmpty()) {
            throw new Exception("Ingredient item not found");
        }
        IngredientsItem ingredientsItemSaved = ingredientsItem.get();
        ingredientsItemSaved.setInStock(!ingredientsItemSaved.isInStock());
        return ingredientItemRepository.save(ingredientsItemSaved);
    }
}
