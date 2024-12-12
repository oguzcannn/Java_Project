package org.example.Backend;

import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);
        } catch (IOException e) {
            System.out.println("Server creation error: " + e.getMessage());
        }
    }

    public void startServer() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Error while accepting client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1237); // Server portu
        server.startServer();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error in creating IO streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                out.write("Echo: " + message + "\n");
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Error in message reading: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Error in closing resources: " + e.getMessage());
            }
        }
    }
}

