package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Client;

import java.util.List;

public interface ClientsService {

    List<Client> getClients();

    void saveClient(Client client);

}
