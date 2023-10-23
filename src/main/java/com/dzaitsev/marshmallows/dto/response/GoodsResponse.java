package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.Good;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GoodsResponse {

    private List<Good> items;
}
