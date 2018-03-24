package br.com.iftm.sd.client;

import lombok.*;

import java.net.Socket;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientInformation {

    private String name;
    private String host;
    private String ipAddress;
    private Integer serverPort;

    public ClientInformation(String name,  Socket socket) {
        this.name = name;
        this.host = socket.getInetAddress().getHostName();
        this.ipAddress = socket.getInetAddress().getHostAddress();
        this.serverPort = socket.getPort();
    }
}
