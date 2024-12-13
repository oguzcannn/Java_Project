package org.example.Backend;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatService {
    private final MongoCollection<Document> chatCollection;
    private final ExecutorService executor;

    public ChatService() {
        String connectionString = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserSystem");
        chatCollection = database.getCollection("Chats");
        executor = Executors.newSingleThreadExecutor();
    }

    public String createChat(List<String> participants) {
        String chatId = "CHAT-" + System.currentTimeMillis();
        Document chatDoc = new Document("chatId", chatId)
                .append("participants", participants)
                .append("messages", new ArrayList<>());
        chatCollection.insertOne(chatDoc);
        System.out.println("Yeni bir sohbet oluşturuldu: " + chatId);
        return chatId;
    }

    public void addMessage(String chatId, Message message) {
        Document messageDoc = new Document("messageId", message.getMessage_id())
                .append("senderId", message.getSender_id())
                .append("receiverId", message.getReceiver_id())
                .append("messageContent", message.getMessage_content())
                .append("timestamp", message.getMessage_id());

        chatCollection.updateOne(Filters.eq("chatId", chatId), Updates.push("messages", messageDoc));
        System.out.println("Mesaj eklendi: " + message.getMessage_id());
    }

    public void listenForChanges() {
        executor.submit(() -> {
            ChangeStreamIterable<Document> changeStream = chatCollection.watch();
            for (ChangeStreamDocument<Document> change : changeStream) {
                Document fullDocument = change.getFullDocument();
                if (fullDocument != null) {
                    System.out.println("Değişiklik algılandı: " + fullDocument.toJson());
                }
            }
        });
    }

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
        Document chatDoc = chatCollection.find(Filters.and(
                Filters.all("participants", userId1),
                Filters.all("participants", userId2)
        )).first();

        if (chatDoc != null) {
            return chatDoc.getString("chatId");
        } else {
            System.out.println("Bu kullanıcılar arasında bir sohbet bulunamadı.");
            return null;
        }
    }



}
