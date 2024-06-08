package com.project.ragchatbot.chatbot.savedChat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedChatRepository extends JpaRepository<SavedChat, Integer> {
    public SavedChat findByChatName(String chatName);

    @Query("SELECT s.chatName FROM SavedChat s WHERE s.userID =  :username")
    List<String> findAllChatNames(@Param("username") String username);

    List<SavedChat> findAllByChatNameAndUserID(String chatName, String userID);
}
