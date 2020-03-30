package mathapp.socket.server;

import mathapp.common.Params;

// the Request class is used for logging purposes

public class Request {
    private String id;
    private Params params;
    private String result;

    public String getId() {
        return this.id;
    }

    Request(ServerConnection connection, Params params, int number, String result) {
        this.id = connection.getId() + "R" + number;
        this.params = params;
        this.result = result;
    }
}
