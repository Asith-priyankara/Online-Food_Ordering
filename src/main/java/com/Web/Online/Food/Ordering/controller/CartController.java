package com.Web.Online.Food.Ordering.controller;

import com.Web.Online.Food.Ordering.model.Cart;
import com.Web.Online.Food.Ordering.model.CartItem;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.AddCartItemRequest;
import com.Web.Online.Food.Ordering.request.UpdateCartItemRequest;
import com.Web.Online.Food.Ordering.response.Message;
import com.Web.Online.Food.Ordering.service.CartService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestHeader("Authorization") String jwt,
            @RequestBody AddCartItemRequest addCartItemRequest
            ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem cartItem = cartService.addItemToCart(addCartItemRequest, user);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(cartItem);
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest updateCartItemRequest
    ) throws Exception {
        CartItem cartItem = cartService.updateCartItemQuantity(updateCartItemRequest.getCartItemId(),
                updateCartItemRequest.getQuantity());

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cartItem);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeCartItem (
            @RequestHeader ("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.removeItemFromCart(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Message> clearCart (
            @RequestHeader ("Authorization") String jwt

    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.clearCart(user.getId());

        return  ResponseEntity.
                status(HttpStatus.OK).
                body(new Message("Clear cart successful!"));
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader ("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findCartByUserId(user.getId());

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cart);
    }

}
