package mathapp.socket.server;

import java.util.HashMap;

public class ServerConnectionLog {
    private HashMap<String, ServerConnection> log;

    public ServerConnectionLog() {
        this.log = new HashMap<>();
    }

    public void addItem(String id, ServerConnection connection) {
        this.log.put(id, connection);
    }
}
