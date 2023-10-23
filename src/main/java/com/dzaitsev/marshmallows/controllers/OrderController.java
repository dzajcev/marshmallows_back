package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import com.dzaitsev.marshmallows.dto.response.OrdersResponse;
import com.dzaitsev.marshmallows.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OrdersResponse getOrders(@RequestParam(value = "start", required = false) LocalDate start,
                                    @RequestParam(value = "end", required = false) LocalDate end,
                                    @RequestParam(value = "statuses", required = false) List<OrderStatus> statuses) {
        return new OrdersResponse(ordersService.getOrders(start, end, statuses));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OrdersResponse getOrder(@PathVariable(value = "id") Integer id) {
        return new OrdersResponse(Collections.singletonList(ordersService.getOrder(id)));
    }

    @GetMapping(value = "/delivery", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OrdersResponse getOrdersForDelivery() {
        return new OrdersResponse(ordersService.getOrdersForDelivery());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOrder(@RequestBody Order order) {
        ordersService.saveOrder(order);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteOrder(@PathVariable("id") Integer id) {
        ordersService.deleteOrder(id);
    }

    @GetMapping(value = "/notification/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public boolean clientIsNotificated(@PathVariable("id") Integer id){
        return ordersService.clientIsNotificated(id);
    }
    @PutMapping(value = "/notification/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void setClientIsNotificated(@PathVariable("id") Integer id){
        ordersService.setClientIsNotificated(id);
    }

}
