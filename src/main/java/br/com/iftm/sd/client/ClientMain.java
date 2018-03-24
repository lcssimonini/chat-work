package br.com.iftm.sd.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.UUID;

public class ClientMain {

    private static final String ENTER_YOUR_NAME = "Entre com o seu nome: ";

    public static void main(String[] args) throws IOException {
        Socket connection = getSocketConnection();

        PrintStream output = new PrintStream(connection.getOutputStream());
        BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
        output.print(ENTER_YOUR_NAME);
        String clientName = keyboardReader.readLine();
        output.println(clientName);
        ChatClient chatClient = new ChatClient(connection);
        chatClient.start();

        while (true) {
            if (chatClient.isDone()) {
                break;
            }

            output.println("> ");
            output.println(keyboardReader.readLine());
        }
    }

    private static Socket getSocketConnection() throws IOException {
        return new Socket(UUID.randomUUID().toString(),2000);
    }
}
