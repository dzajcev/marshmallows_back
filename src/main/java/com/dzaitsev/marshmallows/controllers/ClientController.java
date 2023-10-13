package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Client;
import com.dzaitsev.marshmallows.dto.response.ClientResponse;
import com.dzaitsev.marshmallows.service.ClientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {


    private final ClientsService clientsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClients() {
        return new ClientResponse(clientsService.getClients());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveClient(@RequestBody Client client) {
        clientsService.saveClient(client);
    }
}
