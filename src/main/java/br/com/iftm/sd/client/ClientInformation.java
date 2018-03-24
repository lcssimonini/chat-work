package br.com.iftm.sd.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.Socket;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ClientInformation {

    private String name;
    private String host;
    private String ipAddress;
    private Integer serverPort;
    private Integer index;

    public ClientInformation(String name,  Socket socket) {
        this.name = name;
        this.host = socket.getInetAddress().getHostName();
        this.ipAddress = socket.getInetAddress().get
    }
}
