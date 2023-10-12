package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Good;

import java.util.List;

public interface GoodsService {

    List<Good> getGoods();

    void saveGood(Good good);
}
