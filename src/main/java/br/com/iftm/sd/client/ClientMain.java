package br.com.iftm.sd.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        Socket conexao = new Socket("localhost",2000);

        PrintStream saida = new PrintStream(conexao.getOutputStream());

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Entre com o seu nome: ");

        String meuNome = teclado.readLine();

        saida.println(meuNome);

        ChatClient t = new ChatClient(conexao);

        t.start();

        String linha;

        while (true){
            if (t.isDone()){
                break;
            }

            System.out.println("> ");
            linha = teclado.readLine();
            saida.println(linha);
        }

    }
}
