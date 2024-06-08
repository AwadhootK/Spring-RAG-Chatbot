package com.project.ragchatbot.chatbot.savedChat;

import com.project.ragchatbot.chatbot.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SavedChatRepository extends JpaRepository<SavedChat, Integer> {
    public SavedChat findByChatName(String chatName);

    @Query("SELECT s.chatName FROM SavedChat s")
    List<String> findAllChatNames();

    List<SavedChat> findAllByChatNameAndUserID(String chatName, String userID);
}
