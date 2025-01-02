package com.Web.Online.Food.Ordering.repository;

import com.Web.Online.Food.Ordering.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByOwnerId(long id);

    @Query("SELECT r FROM Restaurant  r WHERE lower(r.name) LIKE lower(concat('%',:query,'%') )" + "OR lower(r.cuisineType) LIKE lower(concat('%', :query, '%') ) ")
    List<Restaurant> findBySearchQuery(String query);
}
