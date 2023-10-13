package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Order;

import java.util.List;

public interface OrdersService {
    void saveOrder(Order order);

    List<Order> getOrders();
}
