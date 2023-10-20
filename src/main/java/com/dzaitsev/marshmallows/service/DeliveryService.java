package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Delivery;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryService {
    void saveDelivery(Delivery delivery);

    Delivery getDelivery(Integer id);

    List<Delivery> getDeliveries(LocalDate start, LocalDate end, List<DeliveryStatus> statuses);

    void deleteDelivery(Integer id);
}
