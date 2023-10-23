package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Client;
import com.dzaitsev.marshmallows.dto.response.ClientResponse;
import com.dzaitsev.marshmallows.service.ClientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {


    private final ClientsService clientsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClients(@RequestParam(value = "is-active", required = false) Boolean isActive) {
        return new ClientResponse(clientsService.getClients(isActive));
    }

    @GetMapping(value = "client-with-orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean clientWithOrders(@PathVariable("id") Integer id) {
        return clientsService.clientWithOrders(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveClient(@RequestBody Client client) {
        clientsService.saveClient(client);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClient(@PathVariable(value = "id") Integer id) {
        return new ClientResponse(Collections.singletonList(clientsService.getClient(id)));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@PathVariable("id") Integer id) {
        clientsService.deleteClient(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void restoreClient(@PathVariable("id") Integer id) {
        clientsService.restoreClient(id);
    }


}
