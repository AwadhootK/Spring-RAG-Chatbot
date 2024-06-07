package com.project.ragchatbot.chatbot.chat;

import com.project.ragchatbot.chatbot.savedChat.SavedChat;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private List<CachedChat> chatCache = new LinkedList<>(); // temporary cache instead of Redis
    public void saveChatToCache(CachedChat cachedChat) {
        System.out.println("Saved " + cachedChat.message + " to cache!");
        chatCache.add(cachedChat);
    }
    public void saveChatsToDatabase(SavedChat savedChat) {
        for (CachedChat cachedChat:chatCache) {
            Chat newChat = new Chat();
            newChat.setMessage(cachedChat.message);
            newChat.setChatRole(cachedChat.chatRole);
            newChat.setSavedChat(savedChat);
            chatRepository.save(newChat);
        }
        System.out.println("Saved " + savedChat.getChatID() + " to database!");
        chatCache.clear();
    }

    public List<Chat> findChatsByID(SavedChat savedChat) throws Exception {
        return chatRepository.findChatsBySavedChatOrderByMessageID(savedChat).orElseThrow(Exception::new);
    }
}
