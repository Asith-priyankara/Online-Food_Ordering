package com.Web.Online.Food.Ordering.request;

import com.Web.Online.Food.Ordering.model.Address;

public class OrderRequest {

    private Long restaurantId;
    private Address address;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
