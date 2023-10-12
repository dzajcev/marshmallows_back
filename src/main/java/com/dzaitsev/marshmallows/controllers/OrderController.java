package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.service.OrdersService;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOrder(@RequestBody Order order) {
        ordersService.saveOrder(order);
    }
}
