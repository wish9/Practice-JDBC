package com.codestates.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Member {
    @Id // 식별자로 지정, DB MEMBER 테이블과 매핑
    private long memberId;

    private String email;

    private String name;

    private String phone;
}
