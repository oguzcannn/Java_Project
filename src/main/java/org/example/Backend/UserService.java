package org.example.Backend;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> chatCollection;
    public UserService() {
        try {
            String connectionString = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            var mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("UserSystem");
            userCollection = database.getCollection("Users");
            chatCollection = database.getCollection("Chats");

        } catch (Exception e) {
            System.err.println("An error occurred while connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void registerUser(User user) {
        Document existingUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null,"This username is already taken!","error",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getUsername(), "")){
            JOptionPane.showMessageDialog(null,"Username cannot be empty!","error",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getUserNumber(), "")){
            JOptionPane.showMessageDialog(null,"User number cannot be empty!","error",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getPassword(), "")){
            JOptionPane.showMessageDialog(null,"Password cannot be empty!","error",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(Objects.equals(user.getEmail(), "")){
            JOptionPane.showMessageDialog(null,"Email cannot be empty!","error",JOptionPane.PLAIN_MESSAGE);
            return;
        }


        Document doc = new Document("username", user.getUsername())
                .append("userNumber", user.getUserNumber())
                .append("password", user.getPassword())
                .append("email", user.getEmail())
                .append("createdAt", java.time.Instant.now().toString());
        userCollection.insertOne(doc);
        System.out.println("User has been successfully registered!");
    }
    public boolean isUsernameExists(String username) {
        Document query = new Document("username", username);
        Document user = userCollection.find(query).first();
        return user != null; // user bulunduysa true döner
    }
    public boolean loginUser(String username, String password) {
        Document query = new Document("username", username).append("password", password);
        Document user = userCollection.find(query).first();
        return user != null; // user bulunduysa true döner üsttekinden farkı login kısmında şifre de doğruluyor
    }
    //şifre değiştirme
    public void changePassword(String userName, String newPassword){
        Document query = new Document("username", userName);
        Document update = new Document("$set", new Document("password", newPassword));
        userCollection.updateOne(query, update);
        System.out.println("User has been successfully registered!");
    }

    public void changeUserName(String userName, String newUserName) {
        // usernaem güncelle
        Document existingUser = userCollection.find(new Document("username", newUserName)).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null,"User has been successfully registered!","error",JOptionPane.PLAIN_MESSAGE);
            System.out.println("Error: This username already exists!");
            return;
        }
        Document query = new Document("username", userName);
        Document update = new Document("$set", new Document("username", newUserName));
        userCollection.updateOne(query, update);
        System.out.println("Username has been successfully updated!");

        // kullanıcıların arkadaş listelerini güncelle
        for (Document user : userCollection.find()) {
            List<String> friends = (List<String>) user.get("friends");
            if (friends != null && friends.contains(userName)) {
                // kullanıcı adını arkadaş listesinden çıkarıp yenisini ekle
                friends.remove(userName);
                friends.add(newUserName);

                // kullanıcı belgesini güncelle
                Document friendUpdate = new Document("$set", new Document("friends", friends));
                userCollection.updateOne(new Document("username", user.getString("username")), friendUpdate);
            }
        }
        // Chats koleksiyonundaki participants listesini de güncelle
        for (Document chat : chatCollection.find()) {
            List<String> participants = (List<String>) chat.get("participants");
            if (participants != null && participants.contains(userName)) {
                // kullanıcı adını participants listesinden çıkarıp yenisini ekle
                participants.remove(userName);
                participants.add(newUserName);

                //Chat belgesini güncelle
                Document chatUpdate = new Document("$set", new Document("participants", participants));
                chatCollection.updateOne(new Document("_id", chat.get("_id")), chatUpdate);
            }
        }
        JOptionPane.showMessageDialog(null,"The name of this user has been changed.","error",JOptionPane.PLAIN_MESSAGE);
        System.out.println("The username in the friend lists has been successfully updated!");
    }

    public boolean deleteUser(String username) {
        // user bulma
        Document query = new Document("username", username);
        Document user = userCollection.find(query).first();

        if (user != null) {
            // 1. kullanıcının arkadaş listesinden silinmesi
            for (Document otherUser : userCollection.find()) {
                List<String> friends = (List<String>) otherUser.get("friends");
                if (friends != null && friends.contains(username)) {
                    // kullanıcı adı arkadaş listesinden çıkarılıyor
                    friends.remove(username);

                    // güncellenmiş arkadaş listesini kaydet
                    Document friendUpdate = new Document("$set", new Document("friends", friends));
                    userCollection.updateOne(new Document("username", otherUser.getString("username")), friendUpdate);
                }
            }

            // kullanıcıyı chat'lerden tamamen sil
            chatCollection.deleteMany(Filters.all("participants", username));

            // kullanıcınının silinmesi
            userCollection.deleteOne(query);
            System.out.println("User has been successfully deleted!");

            return true;
        }

        return false;
    }
    // arkadaş ekleme
    public void addFriend(String currentUser, String friendUsername) {
        Document query = new Document("username", currentUser);
        Document update = new Document("$addToSet", new Document("friends", friendUsername));
        userCollection.updateOne(query, update);
        System.out.println(friendUsername + " successfully added as a friend!");
    }
    //arkadaş listesi alma
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