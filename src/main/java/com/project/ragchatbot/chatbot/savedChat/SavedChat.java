package com.project.ragchatbot.chatbot.savedChat;

import com.project.ragchatbot.chatbot.chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saved_chat")
public class SavedChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer chatID;
    private String userID;
    private String chatName;

    @OneToMany(mappedBy = "savedChat")
    private List<Chat> chats;
}
