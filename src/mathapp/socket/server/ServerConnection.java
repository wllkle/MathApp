package mathapp.socket.server;

import mathapp.Colors;
import mathapp.Params;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ServerConnection {
    private Socket socket;
    private String uuid;
    private String id;
    private int number;
    private ServerConnectionLog log;
    private ArrayList<Request> requests;

    public ServerConnection(Socket socket, int number, ServerConnectionLog log) {
        this.socket = socket;
        this.uuid = UUID.randomUUID().toString().toUpperCase();
        this.id = "[" + Colors.ANSI_CYAN + uuid + Colors.ANSI_RESET + "]";
        this.number = number;
        this.log = log;
        this.requests = new ArrayList<>();

        log.addItem(uuid, this);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getId() {
        return this.id;
    }

    public String getIpAddress() {
        return Colors.ANSI_GREEN + this.socket.getInetAddress().toString().replace('/', ' ').trim() + Colors.ANSI_RESET;
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
        this.log.addItem(this.uuid, this);
        return request;
    }
}
