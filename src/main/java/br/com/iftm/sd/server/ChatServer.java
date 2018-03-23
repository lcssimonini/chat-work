package br.com.iftm.sd.server;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import br.com.iftm.sd.client.AllClientInformation;
import br.com.iftm.sd.client.ClientInformation;
import br.com.iftm.sd.log.DocumentLog;

public class ChatServer extends Thread {

    private static final String LEFT_CHAT = "saiu";
    private static final String DISSE = "disse:";
    private static final String SEPARATOR = " ";

    private static List<PrintStream> clientStreams = new ArrayList<>();
    private Socket socket;
    private DocumentLog writeLogger = new DocumentLog();
    private static List<ClientInformation> connectedClientNames = new ArrayList<>();
    private static List<String> clientsMessages = new ArrayList<>();
    private AllClientInformation allClientInformation = new AllClientInformation();

    public ChatServer(Socket s){
        socket = s;
    }

    public String printAllClientInformation() {
        return allClientInformation.printAllClientInformation();
    }

    public void run() {
        BufferedReader input;
        try {
            input = getServerInput();
            PrintStream saida = new PrintStream(socket.getOutputStream());
            String clientName = input.readLine();
            if (clientName == null) {
                return;
            }
            addClient(saida, clientName);
            String linha = input.readLine();

            while (!isEmpty(linha)) {
                sendToAll(saida, clientName, DISSE,linha);
                clientsMessages.add(clientName + " " + DISSE + " " + linha);
                linha = input.readLine();
            }

            removeClient(saida, clientName);
            writeLogger.writeLog(clientsMessages);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClient(PrintStream saida, String clientName) {
        allClientInformation.addClientInformation(clientName);
        clientStreams.add(saida);

    }

    private void removeClient(PrintStream saida, String clientName) throws IOException {
        sendToAll(saida, clientName, LEFT_CHAT," do Chat!");
        clientsMessages.add(clientName + " " + LEFT_CHAT + " do Chat!");
        allClientInformation.removeClientInformation(clientName);
        clientStreams.remove(saida);
    }

    private BufferedReader getServerInput() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    public void sendToAll(PrintStream saida, String name, String acao, String linha) throws IOException {
        clientStreams.forEach(chat-> {
            if (chat != saida) {
                chat.println(name + SEPARATOR + acao + SEPARATOR + linha);
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
}


