package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.*;
import com.dzaitsev.marshmallows.dao.repository.ClientRepository;
import com.dzaitsev.marshmallows.dao.repository.GoodsRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderRepository;
import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.dto.LinkChannel;
import com.dzaitsev.marshmallows.dto.Order;
import com.dzaitsev.marshmallows.dto.OrderLine;
import com.dzaitsev.marshmallows.exceptions.ClientNotFoundException;
import com.dzaitsev.marshmallows.exceptions.PriceNotFoundException;
import com.dzaitsev.marshmallows.mappers.OrderMapper;
import com.dzaitsev.marshmallows.service.OrdersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final GoodsRepository goodsRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public void saveOrder(Order order) {

        ClientEntity client = clientRepository.findById(order.getClient().getId())
                .orElseThrow(()
                        -> new ClientNotFoundException(String.format("client not found: %s", order.getClient().getName())));

        OrderEntity ord = OrderEntity.builder()
                .client(client)
                .comment(order.getComment())
                .createDate(order.getCreateDate())
                .completeDate(order.getCompleteDate())
                .id(order.getId())
                .deadline(order.getDeadline())
                .deliveryAddress(order.getDeliveryAddress())
                .prePaymentSum(order.getPrePaymentSum())
                .shipped(order.getShipped())
                .linkChannel(LinkChannel.PHONE)
                .build();
        client.getOrders().add(ord);
        List<Integer> goodIds = order.getOrderLines().stream()
                .map(OrderLine::getGood)
                .map(Good::getId)
                .toList();
        Iterable<GoodEntity> allById = goodsRepository.findAllById(goodIds);
        Map<Integer, GoodEntity> goodsMap = StreamSupport.stream(allById.spliterator(), false)
                .collect(Collectors.toMap(GoodEntity::getId, y -> y));
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
                    .done(m.getDone())
                    .num(m.getNum())
                    .price(m.getPrice())
                    .realPrice(realPrice)
                    .build();
        }).toList()));
        orderRepository.save(ord);
    }

    @Override
    @Transactional
    public List<Order> getOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(orderMapper::toDto).toList();
    }
}
