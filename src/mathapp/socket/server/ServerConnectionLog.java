package mathapp.socket.server;

import java.util.HashMap;

// This class is only used to keep a track of previous connections

public class ServerConnectionLog {
    private HashMap<String, ServerConnection> log;

    public ServerConnectionLog() {
        this.log = new HashMap<>();
    }

    void addItem(String id, ServerConnection connection) {
        this.log.put(id, connection);
    }
}
