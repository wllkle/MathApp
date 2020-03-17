package mathapp.socket.server;

import mathapp.Colors;
import mathapp.Params;

import java.util.UUID;

public class Request {
    private String id;
    private Params params;
    private int number;
    private Object result;

    public String getId() {
        return id;
    }
    public Params getParams() {
        return params;
    }
    public int getNumber() {
        return number;
    }

    Request(Params params, int number, Object result) {
        this.id = "[" + Colors.ANSI_CYAN + "REQUEST-" + UUID.randomUUID().toString().toUpperCase() + Colors.ANSI_RESET + "]";
        this.params = params;
        this.number = number;
        this.result = result;
    }
}
