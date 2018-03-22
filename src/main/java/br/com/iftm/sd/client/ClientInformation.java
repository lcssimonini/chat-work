package br.com.iftm.sd.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ClientInformation {

    private String name;
    private Integer index;

    public ClientInformation(String name) {
        this.name = name;
    }
}
