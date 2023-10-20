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
import com.dzaitsev.marshmallows.exceptions.OrderNotFoundException;
import com.dzaitsev.marshmallows.exceptions.PriceNotFoundException;
import com.dzaitsev.marshmallows.mappers.OrderMapper;
import com.dzaitsev.marshmallows.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final GoodsRepository goodsRepository;
    private final OrderMapper orderMapper;

    @Override
    public void saveOrder(Order order) {
        List<Integer> goodIds = order.getOrderLines().stream()
                .map(OrderLine::getGood)
                .map(Good::getId)
                .toList();
        Iterable<GoodEntity> allById = goodsRepository.findAllById(goodIds);
        Map<Integer, GoodEntity> goodsMap = StreamSupport.stream(allById.spliterator(), false)
                .collect(Collectors.toMap(GoodEntity::getId, y -> y));
        ClientEntity client = clientRepository.findById(order.getClient().getId())
                .orElseThrow(()
                        -> new ClientNotFoundException(String.format("client not found: %s", order.getClient().getName())));

        OrderEntity ord = OrderEntity.builder()
                .client(client)
                .phone(order.getPhone())
                .comment(order.getComment())
                .createDate(order.getCreateDate())
                .completeDate(order.getCompleteDate())
                .id(order.getId())
                .deadline(order.getDeadline())
                .deliveryAddress(order.getDeliveryAddress())
                .prePaymentSum(order.getPrePaymentSum())
                .shipped(order.isShipped())
                .paySum(order.getPaySum())
                .needDelivery(order.isNeedDelivery())
                .build();
        client.getOrders().add(ord);

        ord.setOrderLines(new ArrayList<>(order.getOrderLines().stream().map(m -> {
            GoodEntity goodEntity = goodsMap.get(m.getGood().getId());
            PriceEntity realPrice = goodEntity.getPrices().stream()
                    .max(Comparator.comparing(PriceEntity::getCreateDate))
                    .orElseThrow(() -> new PriceNotFoundException(String.format("prices for good %s not found", goodEntity.getId())));
            return OrderLineEntity.builder()
                    .order(ord)
                    .createDate(m.getCreateDate())
                    .good(goodEntity)
                    .count(m.getCount())
                    .id(m.getId())
                    .done(ord.isShipped() || m.isDone())
                    .num(m.getNum())
                    .price(m.getPrice())
                    .realPrice(realPrice)
                    .build();
        }).toList()));
        orderRepository.save(ord);
    }

    @Override
    public Order getOrder(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(String.format("order with id %s not found", id)));
    }

    @Override
    public List<Order> getOrders(LocalDate start, LocalDate end, List<OrderStatus> statuses) {
        return orderRepository.findByCriteria(start, end, statuses).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}
