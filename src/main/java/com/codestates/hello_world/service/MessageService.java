package com.codestates.hello_world.service;

import com.codestates.hello_world.repository.MessageRepository;
import com.codestates.hello_world.entity.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);  // Message 엔티티 클래스에 포함된 데이터를 데이터베이스에 저장
    }
}
