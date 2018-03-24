package br.com.iftm.sd.client;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClient extends Thread {

    public static final String EMPTY_STRING = "";
    @Getter
    private boolean done = false;
    private Socket conexao;

    public ChatClient(Socket s) {
        conexao = s;
    }

    public void run() {
        BufferedReader entrada;

        try {
            entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while (true){
                linha = entrada.readLine();
                if (EMPTY_STRING.equals(linha.trim())) {
                    System.out.println("Conexao encerrada!!!");
                    break;
                }
                printMessage(linha);
            }

            done = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printMessage(String linha) {
        System.out.println("\n"+linha);
        System.out.print("...>\n");
    }
}
