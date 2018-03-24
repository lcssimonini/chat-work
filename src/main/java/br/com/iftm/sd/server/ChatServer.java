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
import com.google.common.collect.ImmutableMap;

public class ChatServer extends Thread {

    private static final String LEFT_CHAT = "saiu";
    private static final String DISSE = "disse:";
    private static final String SEPARATOR = " ";

    private static List<PrintStream> clientStreams = new ArrayList<>();
    private Socket socket;
    private DocumentLog logWritter = new DocumentLog();
    private static List<String> clientsMessages = new ArrayList<>();
    private static AllClientInformation allClientInformation = new AllClientInformation();

    ChatServer(Socket socket) {
        this.socket = socket;
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
            ClientInformation information = new ClientInformation(clientName, socket);
            addClient(saida, information);
            String linha = input.readLine();

            while (!isEmpty(linha)) {
                sendToAll(saida, clientName, DISSE,linha);
                clientsMessages.add(clientName + SEPARATOR + DISSE + SEPARATOR + linha);
                writeMessageLogs(information);
                linha = input.readLine();
                clientsMessages.clear();
            }

            removeClient(saida, clientName);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMessageLogs(ClientInformation information) {
        logWritter.writeLog(ImmutableMap.of(information, clientsMessages));
    }

    private void addClient(PrintStream saida, ClientInformation clientinformation) {
        allClientInformation.addClientInformation(clientinformation);
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


