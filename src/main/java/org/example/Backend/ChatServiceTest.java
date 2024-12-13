package org.example.Backend;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ChatServiceTest {
    public static void main(String[] args) {
        ChatService chatService = new ChatService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Chat Service Test Başladı!");

        while (true) {
            System.out.println("\nLütfen bir seçenek seçin:");
            System.out.println("1. Yeni bir sohbet oluştur");
            System.out.println("2. Mesaj gönder");
            System.out.println("3. Mesajları görüntüle");
            System.out.println("4. Değişiklikleri dinle");
            System.out.println("5. Çıkış");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Enter tuşunu temizle

            switch (choice) {
                case 1:
                    System.out.print("Katılımcıların ID'lerini virgülle ayırarak girin: ");
                    String participantsInput = scanner.nextLine();
                    List<String> participants = Arrays.asList(participantsInput.split(","));
                    String chatId = chatService.createChat(participants);
                    System.out.println("Oluşturulan Chat ID: " + chatId);
                    break;

                case 2:
                    System.out.print("Chat ID'yi girin: ");
                    String chatIdForMessage = scanner.nextLine();
                    System.out.print("Gönderenin ID'sini girin: ");
                    String senderId = scanner.nextLine();
                    System.out.print("Alıcının ID'sini girin: ");
                    String receiverId = scanner.nextLine();
                    System.out.print("Mesaj içeriğini girin: ");
                    String messageContent = scanner.nextLine();

                    Message message = new Message(senderId, receiverId, messageContent);
                    chatService.addMessage(chatIdForMessage, message);
                    break;

                case 3:
                    System.out.print("Chat ID'yi girin: ");
                    String chatIdForViewing = scanner.nextLine();
                    List<Message> messages = chatService.getMessages(chatIdForViewing);
                    System.out.println("Mesajlar:");
                    for (Message msg : messages) {
                        System.out.println("Gönderen: " + msg.getSender_id() + ", Alıcı: " + msg.getReceiver_id() + ", İçerik: " + msg.getMessage_content());
                    }
                    break;

                case 4:
                    System.out.println("Değişiklikleri dinlemeye başlıyoruz...");
                    //chatService.listenForChanges();
                    break;

                case 5:
                    System.out.println("Test sonlandırılıyor...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
                    break;
            }
        }
    }
}
