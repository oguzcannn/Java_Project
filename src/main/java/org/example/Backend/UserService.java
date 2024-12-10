package org.example.Backend;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserService {
    private final MongoCollection<Document> userCollection;

    public UserService() {
        String connectionString = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        var mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserSystem");
        userCollection = database.getCollection("Users");
    }

    public void registerUser(User user) {
        Document doc = new Document("username", user.getUsername())
                .append("userNumber", user.getUserNumber())
                .append("password", user.getPassword()) // Şifreleme eklenebilir
                .append("email", user.getEmail())
                .append("createdAt", java.time.Instant.now().toString());
        userCollection.insertOne(doc);
        System.out.println("Kullanıcı başarıyla kaydedildi!");
    }

    public boolean loginUser(String username, String password) {
        Document query = new Document("username", username).append("password", password);
        Document user = userCollection.find(query).first();
        return user != null; // Kullanıcı bulunduysa true döner
    }
}

