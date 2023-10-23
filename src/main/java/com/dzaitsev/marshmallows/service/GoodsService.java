package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.Good;

import java.util.List;

public interface GoodsService {

    List<Good> getGoods(Boolean isActive);

    Good getGood(Integer id);

    void saveGood(Good good);

    void deleteGood(Integer id);

    void restoreGood(Integer id);

    boolean goodWithOrderLines(Integer id);
}
