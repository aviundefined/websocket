package com.rachvik.websocket.service;

import com.rachvik.websocket.model.ChatRoom;
import com.rachvik.websocket.repository.ChatRoomRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  public Optional<String> getChatroomId(
      final String senderId, final String recipientId, final boolean createNewRoomIfNotExists) {
    return chatRoomRepository
        .findBySenderIdAndRecipientId(senderId, recipientId)
        .map(ChatRoom::getChatId)
        .or(
            () -> {
              if (createNewRoomIfNotExists) {
                return Optional.of(createChat(senderId, recipientId));
              }
              return Optional.empty();
            });
  }

  private String createChat(String senderId, String recipientId) {
    val chatId = String.format("%s_%s", senderId, recipientId);
    chatRoomRepository.save(
        ChatRoom.builder().senderId(senderId).recipientId(recipientId).chatId(chatId).build());
    chatRoomRepository.save(
        ChatRoom.builder().senderId(recipientId).recipientId(senderId).chatId(chatId).build());
    return chatId;
  }
}
