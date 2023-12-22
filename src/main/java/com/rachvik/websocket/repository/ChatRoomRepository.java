package com.rachvik.websocket.repository;

import com.rachvik.websocket.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
  Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
