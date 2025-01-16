package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.model.Cart;
import com.Web.Online.Food.Ordering.model.CartItem;
import com.Web.Online.Food.Ordering.model.Food;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.repository.CartItemRepository;
import com.Web.Online.Food.Ordering.repository.CartRepository;
import com.Web.Online.Food.Ordering.request.AddCartItemRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final FoodService foodService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(FoodService foodService, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.foodService = foodService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem addItemToCart (AddCartItemRequest addCartItemRequest, User user) throws Exception {
        Food food = foodService.findFoodById(addCartItemRequest.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());

        for (CartItem cartItem : cart.getItem()) {
            if (cartItem.getFood().equals(food)) {
                int newQuantity = cartItem.getQuantity() + addCartItemRequest.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(addCartItemRequest.getQuantity());
        newCartItem.setIngredients(addCartItemRequest.getIngredients());
        newCartItem.setTotalPrice(addCartItemRequest.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        return savedCartItem;
    }

    public CartItem updateCartItemQuantity(Long cartItemId, int newQuantity) throws Exception {
        Optional <CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            throw new Exception("CartItem not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(cartItem.getFood().getPrice() * newQuantity);

        return cartItemRepository.save(cartItem);
    }

    public Cart removeItemFromCart(Long cartItemId, User user) throws Exception {
        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            throw new Exception("CartItem not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cart.getItem().remove(cartItem);

        return cart;
    }

    public Long calculateCartTotals (Cart cart) throws Exception {
        Long total = 0L;
        for (CartItem cartItem : cart.getItem()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    public Cart findCartById(Long id) throws Exception {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }
        return cartOptional.get();
    }

    public Cart findCartByUserId (Long userId) throws Exception {
        return cartRepository.findByCustomerId(userId);
    }

    public Cart clearCart (Long userId) throws Exception {
        Cart cart = findCartByUserId(userId);

        cart.getItem().clear();
        return cartRepository.save(cart);
    }
}
