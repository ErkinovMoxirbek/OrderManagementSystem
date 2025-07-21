package com.example.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {
    private final Set<String> activeRooms = Collections.synchronizedSet(new HashSet<>());

    public void addRoom(String roomId) {
        activeRooms.add(roomId);
    }

    public Set<String> getAllActiveRooms() {
        return activeRooms;
    }
}
