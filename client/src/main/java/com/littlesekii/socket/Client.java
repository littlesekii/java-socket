package com.littlesekii.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

public class Client {
    
    public static void main(String[] args) throws IOException {

        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader ioReader = LineReaderBuilder.builder().terminal(terminal).build();
        
        String address = "localhost";
        int port = 8080;

        String username;

        try (Socket client = new Socket(address, port)) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
            
            System.out.println("Connected to server (" + address + ":" + port + ")"); 
        
            BufferedReader socketReader = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );

            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            System.out.print("Enter your username: ");
            username = ioReader.readLine();

            writer.println("USERNAME:" + username);

            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();

            Thread messageWatchThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = socketReader.readLine()) != null) {
                        ioReader.printAbove(message);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            messageWatchThread.start();

            while (true) {
                String message = ioReader.readLine("> ");

                terminal.writer().print("\033[1A");
                terminal.writer().print("\033[2K");
                terminal.flush();

                writer.println("MESSAGE:" + message);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
