package br.com.iftm.sd.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Thread {
    private static boolean done = false;
    private Socket conexao;

    public ChatClient(Socket s){
        conexao = s;
    }

    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket conexao = new Socket("localhost",2000);

        PrintStream saida = new PrintStream(conexao.getOutputStream());

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Entre com o seu nome: ");

        String meuNome = teclado.readLine();

        saida.println(meuNome);

        Thread t = new ChatClient(conexao);

        t.start();

        String linha;

        while (true){
            if (done){
                break;
            }

            System.out.println("> ");
            linha = teclado.readLine();
            saida.println(linha);
        }

    }

    public void run() {
        BufferedReader entrada;

        try {
            entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while (true){
                linha = entrada.readLine();
                if (linha.trim().equals("")){
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
