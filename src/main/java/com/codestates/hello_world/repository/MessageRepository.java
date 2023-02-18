package com.codestates.hello_world.repository;


import com.codestates.hello_world.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    // 데이터의 생성, 조회, 수정, 삭제 작업을 위한 별도의 코드를 구현하지 않아도 CrudRepository를 상속받은 이 인터페이스가 대신 구현해 줌
}
