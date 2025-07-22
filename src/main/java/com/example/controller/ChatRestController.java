package com.example.controller;

import com.example.dto.MessageDTO;
import com.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/rooms/{roomId}")
    public List<MessageDTO> getRoomMessages(@PathVariable String roomId) {
        return chatService.getMessages(roomId);
    }
    @GetMapping("/rooms")
    public Set<Object> getAllRooms() {
        return chatService.getAllRooms();
    }
}
