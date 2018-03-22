package br.com.iftm.sd.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static final String WAITING_CONNECTION = "Esperando conectar...";
    private static final String CONNECTED = " Conectou!";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);

        while (true) {
            System.out.println(WAITING_CONNECTION);
            Socket conexao = serverSocket.accept();
            System.out.println(CONNECTED);
            ChatServer server = new ChatServer(conexao);
            server.start();
            System.out.println(server.printAllClientInformation());
        }
    }
}
