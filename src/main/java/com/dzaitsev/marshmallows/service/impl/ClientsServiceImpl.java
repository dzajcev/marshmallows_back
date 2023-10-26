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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientsServiceImpl extends AbstractService implements ClientsService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    @Override
    public List<Client> getClients(Boolean isActive) {
        Iterable<ClientEntity> result;
        if (Boolean.TRUE.equals(isActive)) {
            result = clientRepository.findClientEntitiesByActiveAndUserCreate(true, getUserFromContext().getId());
        } else if (Boolean.FALSE.equals(isActive)) {
            result = clientRepository.findClientEntitiesByActiveAndUserCreate(false, getUserFromContext().getId());
        } else {
            result = clientRepository.findAllByUserCreate(getUserFromContext().getId());
        }
        return  StreamSupport.stream(result.spliterator(), false)
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public boolean clientWithOrders(Integer id) {
        return clientRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(m -> !m.getOrders().isEmpty()).orElse(false);
    }

    @Override
    public Client getClient(Integer id) {
        return clientRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(clientMapper::toDto)
                .orElseThrow(() -> new ClientNotFoundException(String.format("client with id %s not found", id)));
    }

    @Override
    public void saveClient(Client client) {
        ClientEntity cl = null;
        if (client.getId() != null) {
            cl = clientRepository.findByIdAndUserCreate(client.getId(), getUserFromContext().getId()).orElse(null);
        }
        if (cl == null) {
            cl = ClientEntity.builder()
                    .id(client.getId())
                    .build();
        }
        cl.setActive(client.isActive());
        cl.setName(client.getName());
        cl.setComment(client.getComment());
        cl.setDefaultDeliveryAddress(client.getDefaultDeliveryAddress());
        cl.setPhone(client.getPhone());
        cl.setLinkChannels(client.getLinkChannels().stream()
                .map(Enum::name).collect(Collectors.joining(";")));
        clientRepository.save(cl);
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .ifPresent(clientEntity -> {
                    if (!clientEntity.getOrders().isEmpty()) {
                        clientEntity.setActive(false);
                    } else {
                        clientRepository.delete(clientEntity);
                    }
                });
    }

    @Override
    public void restoreClient(Integer id) {
        clientRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .ifPresent(clientEntity -> clientEntity.setActive(true));
    }
}
