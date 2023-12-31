package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dao.repository.DeliveryRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderRepository;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.*;
import com.dzaitsev.marshmallows.exceptions.DeleteDeliveryNotAllowException;
import com.dzaitsev.marshmallows.exceptions.DeliveryExecutorNotFoundException;
import com.dzaitsev.marshmallows.exceptions.DeliveryNotFoundException;
import com.dzaitsev.marshmallows.mappers.DeliveryMapper;
import com.dzaitsev.marshmallows.service.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl extends AbstractService implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMapper deliveryMapper;
    private final UserRepository userRepository;

    @Override
    public void saveDelivery(Delivery delivery) {
        List<Integer> orderIds = delivery.getOrders().stream()
                .map(Order::getId)
                .toList();
        Map<Integer, Boolean> collect = delivery.getOrders().stream().collect(Collectors.toMap(Order::getId,
                y -> y.getOrderStatus() == OrderStatus.SHIPPED));

        DeliveryEntity deliveryEntity = Optional.ofNullable(delivery.getId())
                .flatMap((Function<Integer, Optional<DeliveryEntity>>) id
                        -> deliveryRepository.findById(id, getUserFromContext().getId()))
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
        deliveryEntity.setExecutor(getExecutor(delivery.getExecutor()));
        deliveryEntity.getOrders().forEach(o -> {
            Boolean shipped = collect.get(o.getId());
            if (!shipped) {
                o.setOrderStatus(OrderStatus.IN_DELIVERY);
            } else {
                o.setOrderStatus(OrderStatus.SHIPPED);
            }
            o.setDelivery(deliveryEntity);
        });

        if (deliveryEntity.getOrders() != null && (deliveryEntity.getOrders().stream().allMatch(entity -> entity.getOrderStatus() == OrderStatus.SHIPPED))) {
            deliveryEntity.setDeliveryStatus(DeliveryStatus.DONE);
        } else if (deliveryEntity.getOrders() != null && (deliveryEntity.getOrders().stream().anyMatch(entity -> entity.getOrderStatus() != OrderStatus.SHIPPED)
                && deliveryEntity.getOrders().stream().anyMatch(entity -> entity.getOrderStatus() == OrderStatus.SHIPPED))) {
            deliveryEntity.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        } else {
            deliveryEntity.setDeliveryStatus(DeliveryStatus.NEW);
        }
        deliveryRepository.save(deliveryEntity);
    }

    private UserEntity getExecutor(User user) {
        //todo: codes
        return Optional.ofNullable(user)
                .map(User::getId)
                .or(() -> Optional.of(getUserFromContext().getId()))
                .flatMap(userRepository::findById).orElseThrow(DeliveryExecutorNotFoundException::new);
    }

    @Override
    public Delivery getDelivery(Integer id) {
        return deliveryRepository.findById(id, getUserFromContext().getId())
                .map(deliveryMapper::toDto)
                .orElseThrow(DeliveryNotFoundException::new);
    }

    @Override
    public List<Delivery> getDeliveries(LocalDate start, LocalDate end, List<DeliveryStatus> statuses) {
        return deliveryRepository.getAvailableDeliveries(Optional.ofNullable(start)
                                .map(LocalDate::atStartOfDay).orElse(LocalDateTime.of(2020, 1, 1, 0, 0, 0)),
                        Optional.ofNullable(end)
                                .map(LocalDate::atStartOfDay).orElse(LocalDateTime.of(2050, 1, 1, 0, 0, 0)),
                        Optional.ofNullable(statuses).orElse(new ArrayList<>()), getUserFromContext().getId()).stream()
                .map(deliveryMapper::toDto).toList();
    }

    @Override
    public void deleteDelivery(Integer id) {
        DeliveryEntity deliveryEntity = deliveryRepository.findById(id, getUserFromContext().getId())
                .orElseThrow(DeliveryNotFoundException::new);
        Delivery delivery = deliveryMapper.toDto(deliveryEntity);
        if (allowToDelete(delivery)) {
            deliveryEntity.getOrders().forEach(o -> o.setDelivery(null));
            deliveryEntity.getOrders().clear();
            deliveryRepository.deleteById(id);
        }
    }

    private boolean allowToDelete(Delivery delivery) {
        if (delivery.getDeliveryStatus() != DeliveryStatus.NEW) {
            throw new DeleteDeliveryNotAllowException("Удаление доставки не разрешено, т.к. статус доставки не допускает его удаление");
        }
        return true;
    }
}
