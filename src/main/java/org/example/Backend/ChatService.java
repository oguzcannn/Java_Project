package org.example.Backend;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatService {
    private final MongoCollection<Document> chatCollection;
    private final ExecutorService executor;

    public ChatService() {
        try {
            String connectionString = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("UserSystem");
            chatCollection = database.getCollection("Chats");
            executor = Executors.newSingleThreadExecutor();
        } catch (Exception e) {
            System.err.println("Error: Database connection or collection could not be created!" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("ChatService could not be started, please check the settings.", e);
        }
    }

    public String createChat(List<String> participants) {
        // userları sırasız hale getirmek için sıralama yapıyoruz
        Collections.sort(participants);

        String chatId = findChatIdByParticipants(participants.get(0), participants.get(1));

        if (chatId != null) {
            return chatId;  // eğer sohbet zaten varsa mevcut chatId'yi döndürür
        }

        // eğer sohbet yoksa yeni sohbet oluşturur
        chatId = "CHAT-" + System.currentTimeMillis();
        Document chatDoc = new Document("chatId", chatId)
                .append("participants", participants)
                .append("messages", new ArrayList<>());

        // yeni sohbet veritabanına ekleniyor
        chatCollection.insertOne(chatDoc);
        System.out.println("A new chat has been created: " + chatId);

        return chatId;
    }

    public void addMessage(String chatId, Message message) {
        Document messageDoc = new Document("messageId", message.getMessage_id())
                .append("senderId", message.getSender_id())
                .append("receiverId", message.getReceiver_id())
                .append("messageContent", message.getMessage_content())
                .append("timestamp", message.getMessage_id());

        chatCollection.updateOne(Filters.eq("chatId", chatId), Updates.push("messages", messageDoc));
        System.out.println("Message added: " + message.getMessage_id());
    }

        // mesajları getiren fonksiyon
    public List<Message> getMessages(String chatId) {
        Document chatDoc = chatCollection.find(Filters.eq("chatId", chatId)).first();
        if (chatDoc != null) {
            List<Document> messageDocs = (List<Document>) chatDoc.get("messages");
            List<Message> messages = new ArrayList<>();
            for (Document doc : messageDocs) {
                Message message = new Message(
                        doc.getString("senderId"),
                        doc.getString("receiverId"),
                        doc.getString("messageContent")
                );
                messages.add(message);
            }
            return messages;
        }
        return new ArrayList<>();
    }

    public String findChatIdByParticipants(String userId1, String userId2) {
        List<String> participants = new ArrayList<>();
        participants.add(userId1);
        participants.add(userId2);
        Collections.sort(participants);

        Document chatDoc = chatCollection.find(Filters.all("participants", participants)).first();

        if (chatDoc != null) {
            return chatDoc.getString("chatId");  // eğer sohbet varsa chatin idsi döndürülür
        } else {
            System.out.println("No chat found between these users.");
            return null;  // eğer sohbet yoksa null döndürülür
        }
    }


}
