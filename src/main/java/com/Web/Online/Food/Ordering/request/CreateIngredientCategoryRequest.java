package com.Web.Online.Food.Ordering.request;

public class CreateIngredientCategoryRequest {
    private String categoryName;
    private Long restaurantId;


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
