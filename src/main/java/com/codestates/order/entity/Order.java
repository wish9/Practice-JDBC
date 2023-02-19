package com.codestates.order.entity;

import com.codestates.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Table("ORDERS") // 엔티티와 매핑할 테이블을 지정, 생략 시 매핑한 엔티티 이름을 매핑할 테이블 이름으로 사용
// ‘Order’라는 단어는 SQL 쿼리문에서 사용하는 예약어이기 때문에 테이블 이름을 변경
public class Order {
    @Id // 기본 키 매핑(식별자로 지정)
    private long orderId;

    // 매핑 규칙 3번 = 애그리거트 루트와 애그리거트 루트 간에는 객체로 직접 참조하는 것이 아니라 ID로 참조한다.
    private AggregateReference<Member, Long> memberId; // 테이블 외래키처럼 memberId를 추가해서 참조하도록 한다.
    // AggregateReference클래스로 Member클래스를 감싸면 직접적인 객체 참조가 아닌 ID 참조가 이루어진다.

    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_COFFEE_ID")
    private Set<CoffeeRef> orderCoffees = new LinkedHashSet<>(); // CoffeeRef클래스와 1대N 관계 연결

    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST; // 주문 상태 정보를 나타내는 멤버 변수, 밑에 선언한 OrderStatus enum 타입
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}

