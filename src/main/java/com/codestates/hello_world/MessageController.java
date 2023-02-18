package com.codestates.hello_world;

import com.codestates.hello_world.dto.MessagePostDto;
import com.codestates.hello_world.entity.Message;
import com.codestates.hello_world.mapper.MessageMapper;
import com.codestates.hello_world.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/v1/messages")
@RestController
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper mapper;

    public MessageController(MessageService messageService,
                             MessageMapper mapper) {
        this.messageService = messageService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMessage(
            @Valid @RequestBody MessagePostDto messagePostDto) {
        Message message = messageService.createMessage(mapper.messageDtoToMessage(messagePostDto)); // DI 된 Repository의 메서드를 사용

        return ResponseEntity.ok(mapper.messageToMessageResponseDto(message));
    }
}
