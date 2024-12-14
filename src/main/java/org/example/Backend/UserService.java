package org.example.Backend;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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
    public boolean isUsernameExists(String username) {
        Document query = new Document("username", username);
        Document user = userCollection.find(query).first();
        return user != null; // Kullanıcı bulunduysa true döner
    }
    public boolean loginUser(String username, String password) {
        Document query = new Document("username", username).append("password", password);
        Document user = userCollection.find(query).first();
        return user != null; // Kullanıcı bulunduysa true döner
    }
    public void changePassword(String userName, String newPassword){
        Document query = new Document("username", userName);
        Document update = new Document("$set", new Document("password", newPassword));
        userCollection.updateOne(query, update);
        System.out.println("Şifre başarıyla güncellendi!");
    }

    public void changeUserName(String userName, String newUserName) {
        // Kullanıcı adını güncelle
        Document query = new Document("username", userName);
        Document update = new Document("$set", new Document("username", newUserName));
        userCollection.updateOne(query, update);
        System.out.println("Kullanıcı adı başarıyla güncellendi!");

        // Diğer kullanıcıların arkadaş listelerini güncelle
        for (Document user : userCollection.find()) {
            List<String> friends = (List<String>) user.get("friends");
            if (friends != null && friends.contains(userName)) {
                // Eski kullanıcı adını arkadaş listesinden çıkarıp yenisini ekle
                friends.remove(userName);
                friends.add(newUserName);

                // Kullanıcı belgesini güncelle
                Document friendUpdate = new Document("$set", new Document("friends", friends));
                userCollection.updateOne(new Document("username", user.getString("username")), friendUpdate);
            }
        }
        System.out.println("Arkadaş listelerindeki kullanıcı adı başarıyla güncellendi!");
    }

    public boolean deleteUser(String username) {
        Document query = new Document("username", username);
        Document user = userCollection.find(query).first();
        if (user != null) {
            userCollection.deleteOne(query);
            System.out.println("Kullanıcı başarıyla silindi!");
            return true;
        }
        return false;  // Kullanıcı bulunamadıysa false döner
    }
    public void addFriend(String currentUser, String friendUsername) {
        Document query = new Document("username", currentUser);
        Document update = new Document("$addToSet", new Document("friends", friendUsername));
        userCollection.updateOne(query, update);
        System.out.println(friendUsername + " başarıyla arkadaş olarak eklendi!");
    }

    public List<String> getFriendList(String currentUser) {
        Document query = new Document("username", currentUser);
        Document user = userCollection.find(query).first();

        if (user != null && user.containsKey("friends")) {
            return (List<String>) user.get("friends");
        }

        return new ArrayList<>(); // Kullanıcının arkadaş listesi yoksa boş liste döner
    }


    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (Document user : userCollection.find()) {
            allUsers.add(user.getString("username"));
        }
        return allUsers;
    }



}