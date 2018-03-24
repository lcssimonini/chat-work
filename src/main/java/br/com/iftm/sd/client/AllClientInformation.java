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

    public ClientInformation getClientInformation(Integer index) {
        return clientInformationMap.get(index);
    }

    public void addClientInformation(ClientInformation clientInformation) {
        clientInformationMap.put(clientCount, clientInformation);
        clientCount++;
    }

    public void removeClientInformation(String name) {
        removeInformation(name);
        clientCount--;
    }

    private void removeInformation(String  name) {
        Optional.ofNullable(getKeyByValue(name)).map(k -> clientInformationMap.remove(k));
    }

    private Integer getKeyByValue(String name) {
        for (Map.Entry<Integer, ClientInformation> entry : clientInformationMap.entrySet()) {
            if (Objects.equals(name, entry.getValue().getName())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String printAllClientInformation() {
        StringBuffer buffer = new StringBuffer();
        clientInformationMap.forEach((key, value) -> buffer.append(key).append(" -- ").append(value.getName()).append("\n"));
        String info = buffer.toString();
        return info.substring(0, info.length()-2);
    }
}
