package com.project.ragchatbot.chatbot.chat;

import com.project.ragchatbot.chatbot.savedChat.SavedChat;
import com.project.ragchatbot.security.config.UserSecurityDetails;
import com.project.ragchatbot.security.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

//@Service
//@AllArgsConstructor
//public class ChatService {
//    private final ChatRepository chatRepository;
//    private List<CachedChat> chatCache = new LinkedList<>(); // temporary cache instead of Redis
//    public void saveChatToCache(CachedChat cachedChat) {
//        System.out.println("Saved " + cachedChat.message + " to cache!");
//        chatCache.add(cachedChat);
//    }
//    public void saveChatsToDatabase(SavedChat savedChat) {
//        for (CachedChat cachedChat:chatCache) {
//            Chat newChat = new Chat();
//            newChat.setMessage(cachedChat.message);
//            newChat.setChatRole(cachedChat.chatRole);
//            newChat.setSavedChat(savedChat);
//            chatRepository.save(newChat);
//        }
//        System.out.println("Saved " + savedChat.getChatID() + " to database!");
//        chatCache.clear();
//    }
//
//    public List<Chat> findChatsByID(SavedChat savedChat) throws Exception {
//        return chatRepository.findChatsBySavedChatOrderByMessageID(savedChat).orElseThrow(Exception::new);
//    }
//}
import com.project.ragchatbot.chatbot.savedChat.SavedChat;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    public void saveChatToCache(CachedChat cachedChat) {
        System.out.println("Saved " + cachedChat.message + " to cache!");
        redisTemplate.opsForList().rightPush("cache_key", cachedChat);
    }

    public void saveChatsToDatabase(SavedChat savedChat) {
        List<Object> cachedChats = redisTemplate.opsForList().range("cache_key", 0, -1);
        if (cachedChats != null) {
            List<CachedChat> chatsToSave = cachedChats.stream()
                    .map(obj -> (CachedChat) obj)
                    .toList();

            for (CachedChat cachedChat : chatsToSave) {
                Chat newChat = new Chat();
                newChat.setMessage(cachedChat.message);
                newChat.setChatRole(cachedChat.chatRole);
                newChat.setSavedChat(savedChat);
                chatRepository.save(newChat);
            }
            System.out.println("Saved " + savedChat.getChatID() + " to database!");
            redisTemplate.delete("cache_key");
        }
    }

    public List<Chat> findChatsByID(SavedChat savedChat) throws Exception {
        return chatRepository.findChatsBySavedChatOrderByMessageID(savedChat).orElseThrow(Exception::new);
    }
}

