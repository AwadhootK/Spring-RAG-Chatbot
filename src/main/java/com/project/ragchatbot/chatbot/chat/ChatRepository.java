package com.project.ragchatbot.chatbot.chat;

import com.project.ragchatbot.chatbot.savedChat.SavedChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Optional<List<Chat>> findChatsBySavedChatOrderByMessageID(SavedChat savedChat);
}
