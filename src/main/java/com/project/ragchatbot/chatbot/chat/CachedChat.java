package com.project.ragchatbot.chatbot.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;

@Getter
@Setter
public class CachedChat implements Serializable {
    String message;
    ChatRole chatRole;
    public CachedChat() {}

    public CachedChat(String message, ChatRole chatRole) {
        this.message = message;
        this.chatRole = chatRole;
    }
}
