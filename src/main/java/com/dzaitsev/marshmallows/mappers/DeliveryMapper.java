package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryMapper extends Mapper<Delivery, DeliveryEntity> {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public Delivery toDto(DeliveryEntity delivery) {
        if (delivery == null) {
            return null;
        }
        return Delivery.builder()
                .deliveryDate(delivery.getDeliveryDate())
                .createDate(delivery.getCreateDate())
                .executor(userMapper.toDto(delivery.getExecutor()))
                .id(delivery.getId())
                .end(delivery.getEnd())
                .start(delivery.getStart())
                .deliveryStatus(delivery.getDeliveryStatus())
                .orders(orderMapper.toDto(delivery.getOrders()))
                .createUser(userRepository.findById(delivery.getUserCreate())
                        .map(userMapper::toDto).orElse(null))
                .build();
    }
}
