package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClientResponse {

    private List<Client> clients;
}
