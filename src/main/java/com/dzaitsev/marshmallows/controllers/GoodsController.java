package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.dto.response.GoodsResponse;
import com.dzaitsev.marshmallows.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GoodsResponse getGoods() {
        return new GoodsResponse(goodsService.getGoods());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveGood(@RequestBody Good good) {
        goodsService.saveGood(good);
    }
}
