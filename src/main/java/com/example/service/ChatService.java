package com.example.service;

import com.example.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ChatService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String PREFIX = "chat:";
    private final String ROOMS_KEY = "chat:rooms";

    public void saveMessage(String roomId, MessageDTO message) {
        // Xabarni saqlash
        redisTemplate.opsForList().rightPush(PREFIX + roomId, message);
        // roomId ni rooms setiga qo‘shish
        redisTemplate.opsForSet().add(ROOMS_KEY, roomId);
    }

    public List<MessageDTO> getMessages(String roomId) {
        Objects.requireNonNull(roomId, "roomId cannot be null");

        List<Object> list = redisTemplate.opsForList().range(PREFIX + roomId, 0, -1);

        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream()
                .filter(obj -> obj instanceof MessageDTO)
                .map(obj -> (MessageDTO) obj)
                .toList();
    }

    public Set<Object> getAllRooms() {
        return redisTemplate.opsForSet().members(ROOMS_KEY);
    }
}
