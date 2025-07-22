package com.example.controller;

import com.example.dto.MessageDTO;
import com.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    @MessageMapping("/chat/sendMessage") // klientlardan kelgan xabar uchun
    @SendTo("/topic/chat/messages") // xabarlarni /topic/chat/messages url-ni subscribe klientlarga jo'natish
    public MessageDTO send(@Payload MessageDTO message) {
        System.out.println("Message from: " + message.getFrom() + " Message body: " + message.getText());
        message.setId(UUID.randomUUID().toString());
        message.setCreatedDate(LocalDateTime.now());
        chatService.saveMessage(message.getId(), message);
        return message;
    }
}
