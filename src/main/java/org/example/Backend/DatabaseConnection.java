package org.example.Backend;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            String uri = "mongodb+srv://javamessage:javamessage@cluster0.f8i4e.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"; // MongoDB'nin çalıştığı adres ve port
            MongoClient mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("UserSystem");
        }
        return database;
    }
}
