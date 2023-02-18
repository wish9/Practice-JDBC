package com.codestates.hello_world.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Message {  // 데이터베이스의 테이블과 매핑할 엔티티(Entity) 클래스
    @Id    // 해당 엔티티의 고유 식별자 역할을 하게 만드는 애너테이션
    private long messageId;
    private String message;
}
