package com.littlesekii.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket client;
    private PrintWriter writer;

    private String username;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );
            writer = new PrintWriter(client.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                String action = message.split(":")[0];
                String value = message.split(":")[1];

                switch (action) {
                    case "USERNAME":
                        this.username = value;
                        break;
                    case "MESSAGE":
                        System.out.println(this.username + ": " + value);
                        broadcast(this.username + ": " + value);
                        break;
                    default:
                        disconnect(client);
                        break;
                }
                    
            }
            System.out.println(message);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            disconnect(client);    
        }
    }

    private void disconnect(Socket client) {
        try {
            System.out.println("Client disconnected (" 
                + client.getInetAddress().getHostAddress() + ":"
                + client.getPort() + ")");
            Server.clientHandlers.remove(this);
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void broadcast(String message) {
        for (ClientHandler clientHandler : Server.clientHandlers) {
            clientHandler.writer.println(message);
        }
    }
}
