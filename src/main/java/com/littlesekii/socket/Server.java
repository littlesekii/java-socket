package com.littlesekii.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    public static List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started in port " + port);

            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected (" 
                    + client.getInetAddress().getHostAddress() + ":"
                    + client.getPort() + ")");

                ClientHandler clientHandler = new ClientHandler(client);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
