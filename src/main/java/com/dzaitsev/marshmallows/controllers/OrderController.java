package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.dto.response.GoodsResponse;
import com.dzaitsev.marshmallows.dto.response.OrdersResponse;
import com.dzaitsev.marshmallows.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OrdersResponse getGoods() {
        return new OrdersResponse(ordersService.getOrders());
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOrder(@RequestBody Order order) {
        ordersService.saveOrder(order);
    }
}
