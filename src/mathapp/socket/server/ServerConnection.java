package mathapp.socket.server;

import mathapp.Colors;
import mathapp.Logger;
import mathapp.Params;

import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection {
    private Socket socket;
    private String id;
    private ServerConnectionLog log;
    private ArrayList<Request> requests;

    public ServerConnection(Socket socket, int number, ServerConnectionLog log) {
        this.socket = socket;
        this.id = "C" + number;
        this.log = log;
        this.requests = new ArrayList<>();

        log.addItem(id, this);
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

    public ArrayList<Request> getRequests() {
        return this.requests;
    }

    public Request addRequest(Params params, int number, Object result) {
        Request request = new Request(this, params, number, result);
        this.requests.add(request);
        this.log.addItem(this.id, this);
        return request;
    }

    public void close() {
        try {
            this.socket.close();
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
}
