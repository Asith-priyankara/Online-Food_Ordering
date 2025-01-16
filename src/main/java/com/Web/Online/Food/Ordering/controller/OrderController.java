package com.Web.Online.Food.Ordering.controller;

import com.Web.Online.Food.Ordering.model.Order;
import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.OrderRequest;
import com.Web.Online.Food.Ordering.service.OrderService;
import com.Web.Online.Food.Ordering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/admin/order/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory (
            @PathVariable Long id,
            @RequestParam(required = false) String orderStatus
    ) throws Exception{
        List<Order> orders = orderService.getRestaurantOrder(id, orderStatus);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(orders);
    }

    @PutMapping("/admin/order/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus (
            @PathVariable Long id,
            @PathVariable String orderStatus
    ) throws Exception{
        Order order = orderService.updateOrder(id, orderStatus);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(order);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder (
            @RequestHeader ("Authentication") String jwt,
            @RequestBody OrderRequest orderRequest
            ) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(orderRequest, user);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(order);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory (
            @RequestHeader ("Authentication") String jwt
    ) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUsersOrder(user.getId());
        return ResponseEntity.
                status(HttpStatus.OK).
                body(orders);
    }

}
