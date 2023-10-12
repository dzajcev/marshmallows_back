package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dto.Client;
import com.dzaitsev.marshmallows.dto.LinkChannel;
import com.dzaitsev.marshmallows.service.ClientsService;
import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import com.dzaitsev.marshmallows.dao.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .map(m -> Client.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .createDate(m.getCreateDate())
                        .phone(m.getPhone())
                        .defaultDeliveryAddress(m.getDefaultDeliveryAddress())
                        .linkChannels(Stream.of(m.getLinkChannels().split(";"))
                                .map(LinkChannel::valueOf).toList())
                        .build())
                .toList();

    }

    @Override
    public void saveClient(Client client) {
        ClientEntity cl = ClientEntity.builder()
                .id(client.getId())
                .name(client.getName())
                .defaultDeliveryAddress(client.getDefaultDeliveryAddress())
                .phone(client.getPhone())
                .linkChannels(client.getLinkChannels().stream()
                        .map(Enum::name).collect(Collectors.joining(";")))
                .build();
        clientRepository.save(cl);
    }
}
