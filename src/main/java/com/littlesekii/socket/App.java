package com.littlesekii.socket;

import java.io.IOException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        // Inicializa o terminal gerenciado da JLine
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    // O printAbove limpa o prompt do usuário, imprime a linha e restaura o prompt intacto!
                    reader.printAbove("------------------");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        while (true) {
            // Fica sempre fixo na última linha com o prompt personalizado
            String line = reader.readLine("Sua mensagem: ");
            reader.printAbove("[Você]: " + line);
        }
    }
}
