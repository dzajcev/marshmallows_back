package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.repository.DeliveryRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderRepository;
import com.dzaitsev.marshmallows.dto.Delivery;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.exceptions.DeliveryNotFoundException;
import com.dzaitsev.marshmallows.mappers.DeliveryMapper;
import com.dzaitsev.marshmallows.service.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final OrderRepository orderRepository;

    private final DeliveryMapper deliveryMapper;

    @Override
    public void saveDelivery(Delivery delivery) {
        List<Integer> orderIds = delivery.getOrders().stream()
                .map(Order::getId)
                .toList();
        Map<Integer, Boolean> collect = delivery.getOrders().stream().collect(Collectors.toMap(Order::getId, Order::isShipped));

        DeliveryEntity deliveryEntity = Optional.ofNullable(delivery.getId())
                .flatMap(deliveryRepository::findById)
                .orElse(new DeliveryEntity());


        Iterable<OrderEntity> orders = orderRepository.findAllById(orderIds);
        deliveryEntity.getOrders().forEach(o -> o.setDelivery(null));
        deliveryEntity.getOrders().clear();
        orderRepository.saveAll(orders);
        List<OrderEntity> orderEntities = StreamSupport.stream(orders.spliterator(), false).toList();
        deliveryEntity.setDeliveryDate(delivery.getDeliveryDate());
        deliveryEntity.setId(delivery.getId());
        deliveryEntity.setEnd(delivery.getEnd());
        deliveryEntity.setStart(delivery.getStart());
        deliveryEntity.getOrders().addAll(orderEntities);
        deliveryEntity.getOrders().forEach(o -> {
            o.setShipped(collect.get(o.getId()));
            o.setDelivery(deliveryEntity);
        });
        deliveryRepository.save(deliveryEntity);

    }

    @Override
    public Delivery getDelivery(Integer id) {
        return deliveryRepository.findById(id)
                .map(deliveryMapper::toDto)
                .orElseThrow(() -> new DeliveryNotFoundException(String.format("delivery with id %s not found", id)));
    }

    @Override
    public List<Delivery> getDeliveries(LocalDate start, LocalDate end, List<DeliveryStatus> statuses) {
        return StreamSupport.stream( deliveryRepository.findAll().spliterator(),false).map(deliveryMapper::toDto).toList();
//        return deliveryRepository.findByCriteria(start, end, statuses).stream()
//                .map(deliveryMapper::toDto).toList();
    }

    @Override
    public void deleteDelivery(Integer id) {
        deliveryRepository.deleteById(id);
    }
}
