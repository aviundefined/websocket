package com.rachvik.websocket.controller;

import com.rachvik.websocket.model.ChatMessage;
import com.rachvik.websocket.model.ChatNotification;
import com.rachvik.websocket.service.ChatMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {
  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;

  @GetMapping("/messages/{senderId}/{recipientId}")
  public ResponseEntity<List<ChatMessage>> findChatMessages(
      @PathVariable("senderId") final String senderId,
      @PathVariable("recipientId") final String recipientId) {
    return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
  }

  @MessageMapping("/chat")
  public void processMessage(@Payload ChatMessage chatMessage) {
    val savedMessage = chatMessageService.save(chatMessage);
    messagingTemplate.convertAndSendToUser(
        chatMessage.getRecipientId(),
        "/queue/messages",
        ChatNotification.builder()
            .chatId(savedMessage.getChatId())
            .senderId(savedMessage.getSenderId())
            .recipientId(savedMessage.getRecipientId())
            .content(savedMessage.getContent())
            .build());
  }
}
