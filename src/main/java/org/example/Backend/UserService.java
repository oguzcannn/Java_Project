package org.example.Backend;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.util.Objects;

public class UserService {
    private final MongoCollection<Document> userCollection;

    public UserService() {
        String connectionString = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        var mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserSystem");
        userCollection = database.getCollection("Users");
    }

    public void registerUser(User user) {
        Document existingUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null,"Bu kullanıcı adı zaten alınmış!","Hata",JOptionPane.PLAIN_MESSAGE);
            return; // Metodu sonlandır
        }
        if(Objects.equals(user.getUsername(), "")){
            JOptionPane.showMessageDialog(null,"Kullanıcı adı boş olamaz!","Hata",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getUserNumber(), "")){
            JOptionPane.showMessageDialog(null,"Kullanıcı numarası boş olamaz!","Hata",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getPassword(), "")){
            JOptionPane.showMessageDialog(null,"Şifre boş olamaz!","Hata",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getEmail(), "")){
            JOptionPane.showMessageDialog(null,"Mail adı boş olamaz!","Hata",JOptionPane.PLAIN_MESSAGE);
            return;
        }


        Document doc = new Document("username", user.getUsername())
                .append("userNumber", user.getUserNumber())
                .append("password", user.getPassword())
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