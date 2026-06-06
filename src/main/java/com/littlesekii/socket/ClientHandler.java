package com.littlesekii.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket client;
    private PrintWriter writer;

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
                if (message.contains("GET")){
                    System.out.println("Client disconnected (" 
                        + client.getInetAddress().getHostAddress() + ":"
                        + client.getPort() + ")");
                    Server.clientHandlers.remove(this);
                    client.close();
                    continue;
                }
                System.out.println(message);
                broadcast(message);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
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

    }

    private void broadcast(String message) {
        for (ClientHandler clientHandler : Server.clientHandlers) {
            clientHandler.writer.println(message);
        }
    }

    

}
