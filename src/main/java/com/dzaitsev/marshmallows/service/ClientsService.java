package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Client;

import java.util.List;

public interface ClientsService {

    List<Client> getClients(Boolean isActive);

    boolean clientWithOrders(Integer id);

    Client getClient(Integer id);

    void saveClient(Client client);

    void deleteClient(Integer id);

    void restoreClient(Integer id);
}
