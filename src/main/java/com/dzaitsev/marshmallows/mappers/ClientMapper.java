package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import com.dzaitsev.marshmallows.dto.Client;
import com.dzaitsev.marshmallows.dto.LinkChannel;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ClientMapper extends Mapper<Client, ClientEntity> {
    @Override
    public Client toDto(ClientEntity clientEntity) {
        return Client.builder()
                .id(clientEntity.getId())
                .name(clientEntity.getName())
                .comment(clientEntity.getComment())
                .createDate(clientEntity.getCreateDate())
                .phone(clientEntity.getPhone())
                .defaultDeliveryAddress(clientEntity.getDefaultDeliveryAddress())
                .linkChannels(Stream.of(clientEntity.getLinkChannels().split(";"))
                        .map(LinkChannel::valueOf).toList())
                .build();
    }
}
