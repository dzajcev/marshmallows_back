package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Delivery;
import com.dzaitsev.marshmallows.dto.response.DeliveryResponse;
import com.dzaitsev.marshmallows.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse getDeliveries() {
        return new DeliveryResponse(deliveryService.getDeliveries(null, null, new ArrayList<>()));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse getDelivery(@PathVariable(value = "id") Integer id) {
        return new DeliveryResponse(Collections.singletonList(deliveryService.getDelivery(id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOrder(@RequestBody Delivery delivery) {
        deliveryService.saveDelivery(delivery);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteDelivery(@PathVariable("id") Integer id) {

    }
}
