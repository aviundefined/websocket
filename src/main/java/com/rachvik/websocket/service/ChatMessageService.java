package com.rachvik.websocket.service;

import com.rachvik.websocket.model.ChatMessage;
import com.rachvik.websocket.repository.ChatMessageRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomService chatRoomService;

  public ChatMessage save(final ChatMessage chatMessage) {
    val chatId =
        chatRoomService
            .getChatroomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
            .orElseThrow();
    chatMessage.setChatId(chatId);
    chatMessageRepository.save(chatMessage);
    return chatMessage;
  }

  public List<ChatMessage> findChatMessages(final String senderId, final String recipientId) {
    return chatRoomService
        .getChatroomId(senderId, recipientId, false)
        .map(chatMessageRepository::findByChatId)
        .orElse(Collections.emptyList());
  }
}
