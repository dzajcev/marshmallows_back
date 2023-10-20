package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import com.dzaitsev.marshmallows.dao.repository.ClientRepository;
import com.dzaitsev.marshmallows.dto.Client;
import com.dzaitsev.marshmallows.exceptions.ClientNotFoundException;
import com.dzaitsev.marshmallows.mappers.ClientMapper;
import com.dzaitsev.marshmallows.service.ClientsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientsServiceImpl implements ClientsService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    @Override
    public List<Client> getClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .map(clientMapper::toDto)
                .toList();

    }

    @Override
    public Client getClient(Integer id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDto)
                .orElseThrow(() -> new ClientNotFoundException(String.format("client with id %s not found", id)));
    }

    @Override
    public void saveClient(Client client) {
        ClientEntity cl = null;
        if (client.getId() != null) {
            cl = clientRepository.findById(client.getId()).orElse(null);
        }
        if (cl == null) {
            cl = ClientEntity.builder()
                    .id(client.getId())
                    .build();
        }
        cl.setName(client.getName());
        cl.setComment(client.getComment());
        cl.setDefaultDeliveryAddress(client.getDefaultDeliveryAddress());
        cl.setPhone(client.getPhone());
        cl.setLinkChannels(client.getLinkChannels().stream()
                .map(Enum::name).collect(Collectors.joining(";")));
        clientRepository.save(cl);
    }
}
