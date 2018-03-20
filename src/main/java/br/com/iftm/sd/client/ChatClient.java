package br.com.iftm.sd.client;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClient extends Thread {

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
                if (linha.trim().equals("")) {
                    System.out.println("Conexao encerrada!!!");
                    break;
                }
                System.out.println();
                System.out.println(linha);
                System.out.print("...> ");
            }

            done = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
