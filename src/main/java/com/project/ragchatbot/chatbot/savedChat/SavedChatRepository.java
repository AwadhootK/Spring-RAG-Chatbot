package com.project.ragchatbot.chatbot.savedChat;

import com.project.ragchatbot.chatbot.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedChatRepository extends JpaRepository<SavedChat, Integer> {
//    public List<Chat> findAllBy
}
