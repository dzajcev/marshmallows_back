package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.Delivery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryResponse {

    private List<Delivery> deliveries;

}
