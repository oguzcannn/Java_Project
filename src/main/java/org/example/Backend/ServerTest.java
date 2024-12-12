package org.example.Backend;


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Aynı portu kullanacağız
        int port = 1238;

        System.out.println("Bu bilgisayar sunucu mu çalıştırıyor? (evet/hayır)");
        String isServer = scanner.nextLine().toLowerCase();

        if (isServer.equals("evet")) {
            // Bu bilgisayar hem sunucu hem istemci
            try {
                // Sunucu tarafı
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Sunucu başlatıldı, bağlanmak için bekleniyor...");

                // Client bağlantısını kabul et
                Socket clientSocket = serverSocket.accept();
                System.out.println("Dış istemci bağlandı!");

                // Sunucu için istemci nesnesi oluştur
                Client serverClient = new Client(clientSocket, "Sunucu");

                // Dış istemciden mesaj okuma
                new Thread(() -> serverClient.readMessages("Sunucu")).start();

                // Bu bilgisayarın kendi istemcisi
                Socket selfSocket = new Socket("localhost", port);
                Client selfClient = new Client(selfSocket, "Sunucu-İstemci");

                // Mesaj gönderme
                while (true) {
                    System.out.println("Mesajınızı yazın ve gönderin:");
                    String message = scanner.nextLine();
                    selfClient.sendMessages("Client", message); // Dış istemciye mesaj gönder
                }
            } catch (IOException e) {
                System.out.println("Sunucu başlatma hatası: " + e.getMessage());
            }
        } else {
            // Dış istemci tarafı
            try {
                System.out.println("Bağlanmak istediğiniz sunucunun IP adresini girin:");
                String serverIP = scanner.nextLine();

                // Dış istemci için socket oluştur
                Socket socket = new Socket(serverIP, port);
                Client client = new Client(socket, "İstemci");

                // Mesajları okuma (asenkron çalışacak)
                client.readMessages("İstemci");

                // Mesaj gönderme
                while (true) {
                    System.out.println("Mesajınızı yazın ve gönderin:");
                    String message = scanner.nextLine();
                    client.sendMessages("Sunucu", message);
                }
            } catch (IOException e) {
                System.out.println("Bağlantı hatası: " + e.getMessage());
            }
        }
    }
}
