package br.com.iftm.sd.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatServer extends Thread {

    private static final String LEFT_CHAT = " saiu ";

    private static List<PrintStream> clientes = new ArrayList<>();
    private Socket conexao;
    private String meuNome;
    private static ArrayList<String> users = new ArrayList<>(Arrays.asList("Digite o número correspondente: 0 - Enviar para todos"));

    public ChatServer(Socket s){
        conexao = s;
    }

    public void run() {
        BufferedReader input;
        try {
            input = getServerInput();
            PrintStream saida = new PrintStream(conexao.getOutputStream());
            meuNome = input.readLine();
            users.add(users.size()+" - "+meuNome);
            if (meuNome == null){
                return;
            }
            clientes.add(saida);
            String linha = input.readLine();

            while ((linha != null)&&(!linha.trim().isEmpty())) {
                sendToAll(saida," disse: ",linha);
                linha = input.readLine();
            }

            sendToAll(saida, LEFT_CHAT," do Chat!");
            clientes.remove(saida);
            conexao.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getServerInput() throws IOException {
        return new BufferedReader(new InputStreamReader(conexao.getInputStream()));
    }


    public void sendToAll(PrintStream saida, String acao, String linha) throws IOException {
        clientes.forEach(chat-> {
            if (chat != saida) {
                chat.println(meuNome + acao + linha);
            }

            if(LEFT_CHAT.equals(acao)) {
                if (saida.equals(chat)) {
                    printEmptyString(chat);
                }
            }
        });
    }

    private void printEmptyString(PrintStream chat) {
        chat.println("");
    }
    
	public void sendChoices() throws IOException {
		PrintStream envio = new PrintStream(conexao.getOutputStream());
		envio.println(this.nomes.toString());
	}
}


