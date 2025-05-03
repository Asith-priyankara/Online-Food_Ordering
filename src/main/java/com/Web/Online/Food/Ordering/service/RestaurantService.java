package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.dto.RestaurantDto;
import com.Web.Online.Food.Ordering.model.Restaurant;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.AddressRepository;
import com.Web.Online.Food.Ordering.repository.RestaurantRepository;
import com.Web.Online.Food.Ordering.repository.UserRepository;
import com.Web.Online.Food.Ordering.request.CreateRestaurantRequest;
import com.Web.Online.Food.Ordering.request.UpdateRestaurantRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, AddressRepository addressRepository, UserRepository userRepository ) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        restaurant.setName(req.getName());
        restaurant.setDescription(req.getDescription());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setAddress(req.getAddress());
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setImages(req.getImages());
        restaurant.setRegistrationDate(LocalDateTime.now());

        addressRepository.save(req.getAddress());
        restaurantRepository.save(restaurant);

        return restaurant;
    }

    public Restaurant updateRestaurant (Long restaurantId, UpdateRestaurantRequest updateRequest, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (!restaurant.getOwner().equals(user)) {
            throw new Exception(restaurant.getName() + "restaurant can not update this user");
        }
        if (updateRequest.getName() != null) {
            restaurant.setName(updateRequest.getName());
        }
        if (updateRequest.getDescription() != null) {
            restaurant.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getCuisineType() != null) {
            restaurant.setCuisineType(updateRequest.getCuisineType());
        }
        if (updateRequest.getAddress() != null) {
            restaurant.setAddress(updateRequest.getAddress());
            addressRepository.save(restaurant.getAddress());
        }
        if (updateRequest.getContactInformation() != null) {
            restaurant.setContactInformation(updateRequest.getContactInformation());
        }
        if (updateRequest.getOpeningHours() != null) {
            restaurant.setOpeningHours(updateRequest.getOpeningHours());
        }
        if (updateRequest.getImages() != null) {
            restaurant.setImages(updateRequest.getImages());
        }
        restaurantRepository.save(restaurant);

        return restaurant;
    }

    public String deleteRestaurant (Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (!restaurant.getOwner().equals(user)) {
            throw new Exception(restaurant.getName() + "restaurant can not delete by this user");
        }
        restaurantRepository.delete(restaurant);
        return restaurant.getName();

    }

    public Restaurant updateRestaurantStatus(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (!restaurant.getOwner().equals(user)) {
            throw new Exception(restaurant.getName() + "restaurant status can not change by this user");
        }
        restaurant.setOpen(!restaurant.isOpen());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        if (restaurant == null) {
            throw new Exception("Restaurant not found with owner id"+ userId);
        }
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> searchRestaurant(String search) {
        return restaurantRepository.findBySearchQuery(search);
    }

    public Restaurant findRestaurantById(Long restaurantId) throws Exception {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isEmpty()) {
            throw new Exception("Restaurant not found");
        } else {
            return restaurant.get();
        }
    }

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setTitle(restaurant.getName());

        if (user.getFavorites().contains(restaurantDto)) {
            user.getFavorites().remove(restaurantDto);
        } else {
            user.getFavorites().add(restaurantDto);
        }
        userRepository.save(user);
        return restaurantDto;

    }

}
