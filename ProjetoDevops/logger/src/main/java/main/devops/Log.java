package main.devops;

import java.sql.Timestamp;

public class Log {
    private Timestamp data;
    private String mensagem;

    public Log(Timestamp data, String mensagem) {
        this.data = data;
        this.mensagem = mensagem;
    }

    public Timestamp getData() {
        return data;
    }

    public String getMensagem() {
        return mensagem;
    }
}
