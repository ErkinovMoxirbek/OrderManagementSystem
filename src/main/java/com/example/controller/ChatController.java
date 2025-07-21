package com.example.controller;

import com.example.dto.ChatMessageDTO;
import com.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO send(@DestinationVariable String roomId, ChatMessageDTO message) {
        message.setRoomId(roomId);
        chatService.addRoom(roomId);
        return message;
    }
}
