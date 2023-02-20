package com.codestates.order.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
public class OrderPostDto {
    @Positive
    private long memberId;

    @Valid // 유효성 검증을 위해 추가
    private List<OrderCoffeeDto> orderCoffees;
}
