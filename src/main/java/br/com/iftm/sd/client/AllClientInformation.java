package br.com.iftm.sd.client;

import lombok.Builder;
import lombok.ToString;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@ToString
public class AllClientInformation {

    @Builder.Default
    private Integer clientCount = 0;
    @Builder.Default
    private Map<Integer, ClientInformation> clientInformationMap = new HashMap<>();

    public void addClientInformation(String name, Socket socket) {
        clientInformationMap.put(clientCount, new ClientInformation(name, socket));
        clientCount++;
    }

    public void removeClientInformation(String name) {
        clientCount--;
        removeInformation(new ClientInformation(name));
    }

    private void removeInformation(ClientInformation information) {
        Optional.ofNullable(getKeyByValue(information)).map(k -> clientInformationMap.remove(k));
    }

    private Integer getKeyByValue(ClientInformation value) {
        for (Map.Entry<Integer, ClientInformation> entry : clientInformationMap.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String printAllClientInformation() {
        StringBuffer buffer = new StringBuffer();
        clientInformationMap.forEach((key, value) -> buffer.append(key).append(" ").append(value).append("\n"));
        return buffer.toString();
    }
}
