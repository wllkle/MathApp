package mathapp.socket.server;

import mathapp.Params;

public class Request {
    private String id;
    private Params params;
    private String result;

    public String getId() {
        return this.id;
    }

    public Params getParams() {
        return this.params;
    }

    Request(ServerConnection connection, Params params, int number, String result) {
        this.id = connection.getId() + "R" + number;
        this.params = params;
        this.result = result;
    }
}
