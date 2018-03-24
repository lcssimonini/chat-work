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
    private static final String GET_USERS_LIST = "lista";
    private static final String SEND_TO = "enviar para ";
    private static final String SEND_TO_SEPARATOR = "-";

    private static List<PrintStream> clientStreams = new ArrayList<>();
    private Socket socket;
    private DocumentLog logWritter = new DocumentLog();
    private static List<String> clientsMessages = new ArrayList<>();
    private static AllClientInformation allClientInformation = new AllClientInformation();

    ChatServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader input;
        try {
            input = getServerInput();
            PrintStream outputStream = new PrintStream(socket.getOutputStream());
            String clientName = input.readLine();
            if (clientName == null) {
                return;
            }
            ClientInformation information = new ClientInformation(clientName, socket, outputStream);
            addClient(outputStream, information);
            String linha = input.readLine();

            while (!isEmpty(linha)) {
                if (linha.contains(GET_USERS_LIST)) {
                    sendUsersList(outputStream);

                } else if (linha.contains(SEND_TO)) {
                    Integer index = getClientIndex(linha);
                    String message = getMessage(clientName, linha);
                    sendToOne(allClientInformation.getClientInformation(index), message);
                } else {
                    sendToAll(outputStream, clientName, DISSE, linha);
                    clientsMessages.add(clientName + SEPARATOR + DISSE + SEPARATOR + linha);
                    writeMessageLogs(information);
                }
                linha = input.readLine();
                clientsMessages.clear();
            }

            removeClient(outputStream, clientName);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMessage(String clientName, String linha) {
        return clientName + SEPARATOR + DISSE + SEPARATOR + linha.split(SEND_TO_SEPARATOR)[2].trim();
    }

    private Integer getClientIndex(String linha) {
        return Integer.valueOf(linha.split("-")[1].trim());
    }

    private void sendUsersList(PrintStream saida) {
        saida.println(allClientInformation.printAllClientInformation());
    }

    private void writeMessageLogs(ClientInformation information) {
        logWritter.writeLog(ImmutableMap.of(information, clientsMessages));
    }

    private void addClient(PrintStream saida, ClientInformation clientinformation) {
        allClientInformation.addClientInformation(clientinformation);
        clientStreams.add(saida);

    }

    private void removeClient(PrintStream saida, String clientName) {
        sendToAll(saida, clientName, LEFT_CHAT," do Chat!");
        clientsMessages.add(clientName + " " + LEFT_CHAT + " do Chat!");
        allClientInformation.removeClientInformation(clientName);
        clientStreams.remove(saida);
    }

    private BufferedReader getServerInput() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    private void sendToAll(PrintStream saida, String name, String acao, String line) {
        clientStreams.forEach(chat-> {
            if (chat != saida) {
                chat.println(name + SEPARATOR + acao + SEPARATOR + line);
            }

            if(LEFT_CHAT.equals(acao)) {
                if (saida.equals(chat)) {
                    printEmptyString(chat);
                }
            }
        });
    }

    private void sendToOne(ClientInformation information, String line) {
        information.getOutputStream().println(line);
    }

    private void printEmptyString(PrintStream chat) {
        chat.println("");
    }
}


