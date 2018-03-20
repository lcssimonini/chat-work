package br.com.iftm.sd.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends Thread {
    private static List<PrintStream> clientes;
    private Socket conexao;
    private String meuNome;

    public ChatServer(Socket s){
        conexao = s;
    }

    public static void main(String[] args) throws IOException {
        clientes = new ArrayList<>();

        ServerSocket s = new ServerSocket(2000);

        while (true){
            System.out.print("Esperando conectar...");
            Socket conexao = s.accept();
            System.out.println(" Conectou!");
            Thread t = new ChatServer(conexao);
            t.start();
        }

    }

    public void run(){
        BufferedReader entrada;
        try {
            entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            PrintStream saida = new PrintStream(conexao.getOutputStream());
            meuNome = entrada.readLine();
            if (meuNome == null){
                return;
            }
            clientes.add(saida);
            String linha = entrada.readLine();
            while ((linha != null)&&(!linha.trim().equals(""))){
                sendToAll(saida," disse: ",linha);
                linha = entrada.readLine();
            }
            sendToAll(saida," saiu "," do Chat!");
            clientes.remove(saida);
            conexao.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendToAll(PrintStream saida, String acao, String linha) throws IOException {
        clientes.forEach(chat-> {
            if (chat != saida) {
                chat.println(meuNome + acao + linha);
            }

            if(acao == " saiu ") {
                if (chat == saida)
                    chat.println("");
            }
        });
    }
}


