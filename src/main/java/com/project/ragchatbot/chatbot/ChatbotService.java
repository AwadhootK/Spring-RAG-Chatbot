package com.project.ragchatbot.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.ragchatbot.chatbot.chat.CachedChat;
import com.project.ragchatbot.chatbot.chat.ChatRole;
import com.project.ragchatbot.chatbot.chat.ChatService;
import com.project.ragchatbot.chatbot.savedChat.SavedChat;
import com.project.ragchatbot.chatbot.savedChat.SavedChatsService;
import com.project.ragchatbot.security.config.UserSecurityDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class ChatbotService {
    private final RestTemplate restTemplate;
    private final ChatService chatService;
    private final SavedChatsService savedChatService;
    private final String FLASK_HOST = "localhost";
    private final String FLASK_PORT = "8000";
    private final String FLASK_URL = "http://" + FLASK_HOST + ":" + FLASK_PORT;


    private String callFlaskAPIPost(String url, String query) {
        String username = UserSecurityDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("query", query);
        requestBody.put("username", username);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private String callFlaskAPIGet(String url) {
        String username = UserSecurityDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url+"/"+username, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    public String ask(String query) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(query);
        String question = jsonNode.get("query").asText();
        chatService.saveChatToCache(new CachedChat(question, ChatRole.USER));

        String answer = callFlaskAPIPost(FLASK_URL + FlaskAPIEndpoints.ASK, query);

        jsonNode = objectMapper.readTree(answer);
        String reply = jsonNode.get("answer").asText();

        chatService.saveChatToCache(new CachedChat(reply, ChatRole.AI));
        return answer;
    }

    public void saveChatsToDB(String chatName) {
        savedChatService.save(UserSecurityDetails.getUsername(), chatName);
    }

    public void emptyContext() {
        callFlaskAPIGet(FLASK_URL + FlaskAPIEndpoints.EMPTY_CONTEXT);
    }

    public String answerLLM(String query) {
        return callFlaskAPIPost(FLASK_URL + FlaskAPIEndpoints.ANSWER_LLM, query);
    }

    public String summarize() {
        return callFlaskAPIGet(FLASK_URL + FlaskAPIEndpoints.SUMMARIZE);
    }

    public String sematicSearch(String query) {
        return callFlaskAPIPost(FLASK_URL + FlaskAPIEndpoints.SEMANTIC_SEARCH, query);
    }

    public List<SavedChat> getAllSavedChats(String chatName) {
        return savedChatService.findAllSavedChats();
    }

    public List<String> getAllSavedChatNames() {
        return savedChatService.findAll();
    }
}
