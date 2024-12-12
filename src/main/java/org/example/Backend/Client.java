package org.example.Backend;

import java.net.*;
import java.io.*;
import java.net.http.WebSocket;

public class Client {
    private Socket testSocket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;


    public Client(Socket socket, String name){
        try{
            this.testSocket = socket;
            this.name = name;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){
        try {
            if(buffReader!= null){
                buffReader.close();
            }
            if (buffWriter != null){
                buffWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            System.out.println("kapamada gönderiminde hata " + e.getMessage());
        }
    }

    public void sendMessages (String recipientId,String messages){
        try {
            if (buffWriter != null){
                buffWriter.write(name + ": " + messages + "kime" + recipientId);
                buffWriter.newLine();
                buffWriter.flush();
            }
        }catch (IOException e){
            System.out.println("mesaj gönderiminde hata " + e.getMessage());
            closeAll(testSocket, buffReader, buffWriter);
        }
    }


    public void readMessages(String selectedFriendId){
        new Thread(()->{
            String serverMessages;
            try{
                while ((serverMessages = buffReader.readLine()) != null){
                    String[] parts = serverMessages.split(":",3);
                    if (parts.length==3){
                        String senderId = parts[0].trim();
                        String recipientId = parts[1].trim();
                        String messegas = parts[2].trim();
                    }else{
                        System.out.println("hatalı mesaj =>" + serverMessages);
                    }
                }
            }catch (IOException e){
                System.out.println("readMessages hatası => " + e);
            }

        }).start();
    }




}
