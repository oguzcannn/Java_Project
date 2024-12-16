package org.example.Backend;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;




public class User implements UserInterface {
    private String username;
    private String userNumber;
    private String password;
    private String email;
    private String[] friend_id;

    public User(String username, String userNumber , String password, String email ) {
        this.username = username;
        this.userNumber = userNumber;
        this.password = password;
        this.email = email;
    }

    // Getter ve Setter metodları
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String[] friend_id) {
        this.friend_id = friend_id;
    }

    public void removeFriend(String friendUsername) {
        MongoDatabase database = DatabaseConnection.getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("Users");
        MongoCollection<Document> chatCollection = database.getCollection("Chats");

        // Kullanıcı belgesini getir
        Document userDoc = userCollection.find(Filters.eq("username", this.username)).first();
        if (userDoc == null) {
            System.out.println("User not found: " + this.username);
            return;
        }

        // arkadaş listesinden çıkarma fonksiyonu
        List<String> friends = (List<String>) userDoc.get("friends");
        if (friends != null && friends.contains(friendUsername)) {
            friends.remove(friendUsername);
            userCollection.updateOne(Filters.eq("username", this.username), Updates.set("friends", friends));
            System.out.println(friendUsername + " removed from the friend list.");
        } else {
            System.out.println(friendUsername + " not found in your friend list.");
            return;
        }

        // karşı tarafın arkadaş listesinden de çıkarmayı unutma
        Document friendDoc = userCollection.find(Filters.eq("username", friendUsername)).first();
        if (friendDoc != null) {
            List<String> friendList = (List<String>) friendDoc.get("friends");
            if (friendList != null && friendList.contains(this.username)) {
                friendList.remove(this.username);
                userCollection.updateOne(Filters.eq("username", friendUsername), Updates.set("friends", friendList));
                System.out.println(this.username + " removed from the other user's friend list.");
            }
        }

        //  bulunduğu chatleri de sil
        List<String> participants = new ArrayList<>();
        participants.add(this.username);
        participants.add(friendUsername);
        participants.sort(String::compareTo);

        Document chatDoc = chatCollection.find(Filters.all("participants", participants)).first();
        if (chatDoc != null) {
            chatCollection.deleteOne(Filters.eq("chatId", chatDoc.getString("chatId")));
            System.out.println("Chat deleted: " + chatDoc.getString("chatId"));
        }
    }
}

