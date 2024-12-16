package org.example.Backend;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.List;

public class Admin extends User implements UserInterface {
    public Admin(String username, String userNumber, String password, String email) {
        super(username, userNumber, password, email);
    }

    // user silme fonksiyonu
    @Override
    public void removeFriend(String usernameToDelete) {
        MongoDatabase database = DatabaseConnection.getDatabase();
        MongoCollection<Document> userCollection = database.getCollection("Users");
        MongoCollection<Document> chatCollection = database.getCollection("Chats");

        // userı sil
        Document userDoc = userCollection.find(Filters.eq("username", usernameToDelete)).first();
        if (userDoc == null) {
            System.out.println("User to be deleted not found: " + usernameToDelete);
            return;
        }
        userCollection.deleteOne(Filters.eq("username", usernameToDelete));
        System.out.println("User deleted: " + usernameToDelete);

        // arkadaşlarının da arkadaş listesinden çıkar
        for (Document otherUserDoc : userCollection.find()) {
            List<String> friends = (List<String>) otherUserDoc.get("friends");
            if (friends != null && friends.contains(usernameToDelete)) {
                friends.remove(usernameToDelete);
                userCollection.updateOne(Filters.eq("username", otherUserDoc.getString("username")), Updates.set("friends", friends));
                System.out.println(usernameToDelete + " removed from other users' friend lists.");
            }
        }

        // kullanıcı ile alakalı sohbetleri sil
        chatCollection.deleteMany(Filters.all("participants", usernameToDelete));
        System.out.println(usernameToDelete + " all chats associated with have been deleted.");
    }
}
