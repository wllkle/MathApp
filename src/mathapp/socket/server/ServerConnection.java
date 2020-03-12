package mathapp.socket.server;

import mathapp.Colors;
import mathapp.Params;

import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

public class ServerConnection {
    private String id;
    private String ipAddress;
    private int number;
    private ArrayList<Request> requests;
    private HashMap<String, ServerConnection> log;

    public ServerConnection(Socket socket, int number, HashMap<String, ServerConnection> log) {
        this.id = "[" + Colors.ANSI_CYAN + "CLIENT-" + UUID.randomUUID().toString().toUpperCase() + Colors.ANSI_RESET + "]";
        this.ipAddress = Colors.ANSI_GREEN + socket.getInetAddress().toString().replace('/', ' ').trim() + Colors.ANSI_RESET;
        this.number = number;
        this.requests = new ArrayList<>();
        this.log = log;

        log.put(this.id, this);
    }

    public String getId() {
        return this.id;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Request> getRequests() {
        return this.requests;
    }

    public Request addRequest(Params params, int number, Object result) {
        Request request = new Request(params, number, result);
        this.requests.add(request);
        this.log.put(this.id, this);
        return request;
    }
}
