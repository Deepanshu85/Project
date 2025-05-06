package com.zosh.repository;

import com.zosh.modal.Order;
import com.zosh.modal.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
