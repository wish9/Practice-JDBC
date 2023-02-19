package com.codestates.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor
@Table("ORDER_COFFEE")
public class CoffeeRef { // Order 클래스와 Coffee 클래스가 N대N 관계이기 때문에 추가한 중간 엔티티
    // Order 클래스와 동일한 애그리거트에 있는 엔티티다.
    @Id
    private long orderCoffeeId;
    //private long orderId;
    private long coffeeId; // N 대 N 관계에서는 AggregateReference로 coffeeId를 감쌀 필요가 없다.
    private int quantity;
}
