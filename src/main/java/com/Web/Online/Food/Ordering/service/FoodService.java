package com.Web.Online.Food.Ordering.service;


import com.Web.Online.Food.Ordering.model.Category;
import com.Web.Online.Food.Ordering.model.Food;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.FoodRepository;
import com.Web.Online.Food.Ordering.request.CreateFoodRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant, User user) throws Exception {
        if (!(user == restaurant.getOwner())) {
            throw new Exception("This user can not add food to this restaurant");
        }
        Food food = new Food();
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setFoodCategory(category);
        food.setImage(request.getImages());
        food.setAvailable(true);
        food.setRestaurant(restaurant);
        food.setVegetarian(request.isVegetarian());
        food.setSeasonal(request.isSeasional());
        food.setIngredientsItems(request.getIngredients());
        food.setCreationDate(new Date(String.valueOf(LocalDateTime.now())));

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    public void deleteFood (Long foodId, User user) throws Exception {
        Food food = foodRepository.findById(foodId).get();
        Restaurant restaurant = food.getRestaurant();
        if (!(user == restaurant.getOwner())) {
            throw new Exception("This user can not delete food from this restaurant");
        }
        food.setRestaurant(null);
        foodRepository.delete(food);
    }

    public List<Food> getRestaurantFood(Long restaurantId, boolean isVegitarain, boolean isNonveg, boolean isSeasonal, String foodCategory) throws Exception {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegitarain) {
            foods = filterByVegetarian(foods);
        }
        if (isNonveg) {
            foods = filterByNonveg(foods);
        }
        if (isSeasonal) {
            foods = filterBySeasonal(foods);
        }
        if (foodCategory != null && foodCategory.equals("")) {
            foods = filterByCategory(foods, foodCategory);
        }
        return foods;
    }

    public List<Food> searchFood (String keyword) {
        return foodRepository.searchFood(keyword);
    }

    public Food findFoodById (Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalFood.isEmpty()) {
            throw new Exception("food not exist");
        }
        return optionalFood.get();
    }

    public Food updateAvailibilityStatus (Long foodId, User user) throws Exception {
        Food food = foodRepository.findById(foodId).get();
        Restaurant restaurant = food.getRestaurant();
        if (!(user == restaurant.getOwner())) {
            throw new Exception("This user can not update food availability of this food in this restaurant");
        }
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }

//    private Food findFoodByFoodId(Long foodId) {
//        return foodRepository.findById(foodId).get();
//    }

    private List<Food> filterByVegetarian(List<Food> foods) {
        return foods.stream().filter(food -> food.isVegetarian() == true).collect(Collectors.toList());
    }

    private List<Food> filterByNonveg(List<Food> foods) {
        return foods.stream().filter(food -> food.isVegetarian() == false).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods) {
        return foods.stream().filter(food -> food.isSeasonal() == true).collect(Collectors.toList());
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if (food.getFoodCategory() != null) {
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

}
