package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Client;

import java.util.List;

public interface ClientsService {

    List<Client> getClients();

    Client getClient(Integer id);

    void saveClient(Client client);

}
