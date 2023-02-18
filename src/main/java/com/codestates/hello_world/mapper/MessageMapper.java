package com.codestates.hello_world.mapper;

import com.codestates.hello_world.dto.MessagePostDto;
import com.codestates.hello_world.dto.MessageResponseDto;
import com.codestates.hello_world.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message messageDtoToMessage(MessagePostDto messagePostDto);
    MessageResponseDto messageToMessageResponseDto(Message message);
}
