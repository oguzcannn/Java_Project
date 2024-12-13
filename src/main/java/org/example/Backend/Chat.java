package org.example.Backend;

import java.util.ArrayList;
import java.util.List;

public class Chat{
    private String chatId;
    private List<String> participants; // İki kullanıcı ID'si
    private List<Message> messages;

    public Chat(String chatId, List<String> participants) {
        this.chatId = chatId;
        this.participants = participants;
        this.messages = new ArrayList<>();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}