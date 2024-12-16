package org.example.Backend;

import java.util.List;

public interface ChatInterface {
    public String getChatId();

    public void setChatId(String chatId);

    public List<String> getParticipants();

    public void setParticipants(List<String> participants);

    public List<Message> getMessages();

    public void setMessages(List<Message> messages);

    public void addMessage(Message message);
}
