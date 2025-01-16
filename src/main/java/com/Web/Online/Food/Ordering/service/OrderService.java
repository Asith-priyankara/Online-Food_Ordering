package com.Web.Online.Food.Ordering.service;

import com.Web.Online.Food.Ordering.model.*;
import com.Web.Online.Food.Ordering.repository.AddressRepository;
import com.Web.Online.Food.Ordering.repository.OrderItemRepository;
import com.Web.Online.Food.Ordering.repository.OrderRepository;
import com.Web.Online.Food.Ordering.repository.UserRepository;
import com.Web.Online.Food.Ordering.request.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final RestaurantService restaurantService;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    public OrderService (RestaurantService restaurantService,
                         CartService cartService,
                         UserRepository userRepository,
                         AddressRepository addressRepository,
                         OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository) {
        this.restaurantService = restaurantService;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder (OrderRequest orderRequest, User user) throws Exception {
        Address shipAddress = orderRequest.getAddress();
        Address savedAddress = addressRepository.save(shipAddress);

        if (!user.getAddresses().contains(shipAddress)) {
            user.getAddresses().add(shipAddress);
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.findRestaurantById(orderRequest.getRestaurantId());

        Order order = new Order();
        order.setCustomer(user);
        order.setRestaurant(restaurant);
        order.setCreatedAt(new Date());
        order.setOrderStatus("PENDING");
        order.setDeliveryAddress(shipAddress);

        Cart cart = cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        Long totalPrice = cartService.calculateCartTotals(cart);

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        restaurant.getOrders().add(savedOrder);

        return order;
    }

    public Order updateOrder (Long orderId, String orderStatus) throws Exception {
        Order order = findOderById(orderId);
        if (orderStatus.equals("PENDING") || orderStatus.equals("COMPLETED") || orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED")) {
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Order status is not PENDING or COMPLETED or OUT_FOR_DELIVERY or DELIVERED");
    }

    public List<Order> getUsersOrder (Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    public List<Order> getRestaurantOrder (Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if (orderStatus != null) {
            orders = orders.stream().filter(
                    order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }
    
    public void cancelOrder (Long orderId, User user) throws Exception {
        Order order = findOderById(orderId);
        orderRepository.deleteById(orderId);
    }

    private Order findOderById(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new Exception("Order not found");
        }
        return order.get();
    }

}
