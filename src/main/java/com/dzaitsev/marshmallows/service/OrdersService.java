package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.dto.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrdersService {
    void saveOrder(Order order);

    Order getOrder(Integer id);

    List<Order> getOrders(LocalDate start, LocalDate end, List<OrderStatus> statuses);

    void deleteOrder(Integer id);

    List<Order> getOrdersForDelivery();

    boolean clientIsNotificated(Integer id);
    void setClientIsNotificated(Integer id);
}
