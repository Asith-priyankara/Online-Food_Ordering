package com.Web.Online.Food.Ordering.repository;

import com.Web.Online.Food.Ordering.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
