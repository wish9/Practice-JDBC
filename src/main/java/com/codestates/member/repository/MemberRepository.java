package com.codestates.member.repository;

import com.codestates.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    // 매개변수 타입 중 Long은 Member 엔티티 클래스에서 @Id 애너테이션이 붙은 멤버 변수의 타입을 가르킨다.
    Optional<Member> findByEmail(String email); // Member테이블의 Email열 값이 email인 row를 찾는 메서드
}
