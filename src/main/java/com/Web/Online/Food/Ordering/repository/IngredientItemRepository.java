package com.Web.Online.Food.Ordering.repository;

import com.Web.Online.Food.Ordering.model.IngredientsItem;
import com.Web.Online.Food.Ordering.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientItemRepository extends JpaRepository <IngredientsItem, Long> {
    List<IngredientsItem> findByRestaurantId(Long restaurantId);
}
