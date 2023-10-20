package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dto.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper extends Mapper<Order, OrderEntity> {

    private final ClientMapper clientMapper;
    private final OrderLineMapper orderLineMapper;
    @Override
    public Order toDto(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .client(clientMapper.toDto(orderEntity.getClient()))
                .comment(orderEntity.getComment())
                .completeDate(orderEntity.getCompleteDate())
                .createDate(orderEntity.getCreateDate())
                .deadline(orderEntity.getDeadline())
                .deliveryAddress(orderEntity.getDeliveryAddress())
                .prePaymentSum(orderEntity.getPrePaymentSum())
                .shipped(orderEntity.isShipped())
                .phone(orderEntity.getPhone())
                .needDelivery(orderEntity.isNeedDelivery())
                .paySum(orderEntity.getPaySum())
                .orderLines(orderEntity.getOrderLines().stream()
                        .map(orderLineMapper::toDto).toList())
                .build();
    }
}
