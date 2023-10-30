package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.*;
import com.dzaitsev.marshmallows.dao.repository.ClientRepository;
import com.dzaitsev.marshmallows.dao.repository.GoodsRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderRepository;
import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.dto.OrderLine;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import com.dzaitsev.marshmallows.exceptions.ClientNotFoundException;
import com.dzaitsev.marshmallows.exceptions.DeleteOrderNotAllowException;
import com.dzaitsev.marshmallows.exceptions.OrderNotFoundException;
import com.dzaitsev.marshmallows.exceptions.PriceNotFoundException;
import com.dzaitsev.marshmallows.mappers.OrderMapper;
import com.dzaitsev.marshmallows.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl extends AbstractService implements OrdersService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final GoodsRepository goodsRepository;
    private final OrderMapper orderMapper;

    @Override
    public void saveOrder(Order order) {

        OrderEntity orderEntity = Optional.ofNullable(order.getId())
                .flatMap((Function<Integer, Optional<OrderEntity>>) id -> orderRepository.findByIdAndUserCreate(id, getUserFromContext().getId()))
                .orElse(new OrderEntity());
        List<Integer> goodIds = order.getOrderLines().stream()
                .map(OrderLine::getGood)
                .map(Good::getId)
                .toList();
        Iterable<GoodEntity> allById = goodsRepository.findAllByIdInAndUserCreate(goodIds, getUserFromContext().getId());
        Map<Integer, GoodEntity> goodsMap = StreamSupport.stream(allById.spliterator(), false)
                .collect(Collectors.toMap(GoodEntity::getId, y -> y));
        ClientEntity client = clientRepository.findByIdAndUserCreate(order.getClient().getId(), getUserFromContext().getId())
                .orElseThrow(()
                        -> new ClientNotFoundException(String.format("client not found: %s", order.getClient().getName())));
        orderEntity.setClient(client);
        orderEntity.setPhone(order.getPhone());
        orderEntity.setComment(order.getComment());
        orderEntity.setCompleteDate(order.getCompleteDate());
        orderEntity.setId(order.getId());
        orderEntity.setDeadline(order.getDeadline());
        orderEntity.setDeliveryAddress(order.getDeliveryAddress());
        orderEntity.setPrePaymentSum(order.getPrePaymentSum());
        orderEntity.setPaySum(order.getPaySum());
        orderEntity.setNeedDelivery(order.isNeedDelivery());
        client.getOrders().add(orderEntity);
        order.getOrderLines()
                .forEach(ol -> {
                    OrderLineEntity orderLineEntity = Optional.ofNullable(ol.getId())
                            .flatMap(m -> orderEntity.getOrderLines().stream().filter(f -> f.getId().equals(m)).findFirst())
                            .orElse(new OrderLineEntity());
                    if (orderLineEntity.getId() == null) {
                        orderEntity.getOrderLines().add(orderLineEntity);
                    }
                    GoodEntity goodEntity = goodsMap.get(ol.getGood().getId());
                    PriceEntity realPrice = goodEntity.getPrices().stream()
                            .max(Comparator.comparing(PriceEntity::getCreateDate))
                            .orElseThrow(() -> new PriceNotFoundException(String.format("prices for good %s not found", goodEntity.getId())));
                    orderLineEntity.setOrder(orderEntity);
                    orderLineEntity.setGood(goodEntity);
                    orderLineEntity.setCount(ol.getCount());
                    orderLineEntity.setDone(orderEntity.getOrderStatus() == OrderStatus.SHIPPED || orderEntity.getOrderStatus() == OrderStatus.IN_DELIVERY || ol.isDone());
                    orderLineEntity.setNum(ol.getNum());
                    orderLineEntity.setPrice(ol.getPrice());
                    orderLineEntity.setRealPrice(realPrice);
                });


        if (orderEntity.getOrderStatus() == OrderStatus.DONE && orderEntity.getCompleteDate() == null) {
            orderEntity.setCompleteDate(LocalDateTime.now());
        } else {
            orderEntity.setOrderStatus(OrderStatus.IN_PROGRESS);
            orderEntity.setCompleteDate(null);
        }
        orderRepository.save(orderEntity);
    }

    @Override
    public Order getOrder(Integer id) {
        return orderRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(String.format("order with id %s not found", id)));
    }

    @Override
    public List<Order> getOrders(LocalDate start, LocalDate end, List<OrderStatus> statuses) {
        return orderRepository.findOrderEntitiesByCreateDateAfterAndCreateDateBeforeAndOrderStatusInAndUserCreate(Optional.ofNullable(start)
                                .map(LocalDate::atStartOfDay).orElse(LocalDateTime.of(2020, 1, 1, 0, 0, 0)),
                        Optional.ofNullable(end)
                                .map(LocalDate::atStartOfDay).orElse(LocalDateTime.of(2050, 1, 1, 0, 0, 0)),
                        Optional.ofNullable(statuses).orElse(new ArrayList<>()), getUserFromContext().getId()).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public void deleteOrder(Integer id) {
        if (allowToDelete(id)) {
            orderRepository.deleteById(id);
        }
    }

    @Override
    public List<Order> getOrdersForDelivery() {
        return orderRepository.getOrdersForDelivery(getUserFromContext().getId())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public boolean clientIsNotificated(Integer id) {
        return orderRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(orderMapper::toDto)
                .map(Order::isClientNotificated)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Заказ %s не найден", id)));
    }


    @Override
    public void setClientIsNotificated(Integer id) {
        OrderEntity orderEntity = orderRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .orElseThrow(() -> new OrderNotFoundException(String.format("Заказ %s не найден", id)));
        orderEntity.setClientNotificated(true);
    }

    private boolean allowToDelete(Integer id) {
        Order order = orderRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Заказ %s не найден", id)));
        if (order.getPrePaymentSum() != null && order.getPrePaymentSum() > 0) {
            throw new DeleteOrderNotAllowException("Удаление заказа не разрешено, т.к. по нему есть предоплата");
        }
        if (order.getPaySum() != null && order.getPaySum() > 0) {
            throw new DeleteOrderNotAllowException("Удаление заказа не разрешено, т.к. по нему есть оплата");
        }
        if (order.getOrderStatus() != OrderStatus.IN_PROGRESS) {
            throw new DeleteOrderNotAllowException("Удаление заказа не разрешено, т.к. статус заказа не допускает его удаление");
        }
        return true;
    }
}
