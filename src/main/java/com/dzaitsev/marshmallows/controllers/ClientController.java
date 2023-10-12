package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.response.ClientResponse;
import com.dzaitsev.marshmallows.service.ClientsService;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {


    private final ClientsService clientsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClients() {
        return new ClientResponse(clientsService.getClients());
    }
}
