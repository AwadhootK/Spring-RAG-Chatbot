package com.project.ragchatbot.chatbot.savedChat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ragchatbot.chatbot.chat.ChatService;
import com.project.ragchatbot.security.config.UserSecurityDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SavedChatsService {
    private final SavedChatRepository savedChatRepository;
    private final ChatService chatService;

    public void save(String username, String chatName) {
        // if chatName already exists mean I just need to add and delete older entry.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(chatName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String chatName1 = jsonNode.get("chatName").asText();

        SavedChat savedChat = savedChatRepository.findByChatName(chatName1);

        if (savedChat == null) {
            savedChat = new SavedChat();
            savedChat.setChatName(chatName1);
            savedChat.setUserID(username);
            savedChatRepository.save(savedChat);
        }
        chatService.saveChatsToDatabase(savedChat);

    }

    public List<SavedChat> findAllSavedChats(String chatName) {
        String username = UserSecurityDetails.getUsername();
        return savedChatRepository.findAllByChatNameAndUserID(chatName, username);
    }

    public List<String> findAll(String username) {
        return savedChatRepository.findAllChatNames(username);
    }
}
