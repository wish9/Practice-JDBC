package com.codestates.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
//@AllArgsConstructor
public class Coffee {
    @Id // 식별자로 지정,  COFFEE 테이블과 매핑
    private long coffeeId;
    private String korName;
    private String engName;
    private Integer price;
    private String coffeeCode; // 중복 등록을 체크하는 용도로 추가
}
