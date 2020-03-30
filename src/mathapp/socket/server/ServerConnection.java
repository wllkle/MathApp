package mathapp.socket.server;

import mathapp.common.Params;
import mathapp.socket.IOSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

// this class is used for managing the server's IOSocket, and also handles logging

public class ServerConnection {
    private IOSocket socket;
    private String id;
    private ServerConnectionLog log;
    private ArrayList<Request> requests;

    public ServerConnection(Socket socket, int number, ServerConnectionLog log) throws IOException {
        this.socket = new IOSocket(socket);
        this.id = "C" + number;
        this.log = log;
        this.requests = new ArrayList<>();

        log.addItem(id, this);
    }

    public IOSocket getSocket() {
        return this.socket;
    }

    public String getId() {
        return this.id;
    }

    public String getIpAddress() {
        return this.socket.getIpAddress();
    }

    public ArrayList<Request> getRequests() {
        return this.requests;
    }

    public Request addRequest(Params params, int number, String result) {
        // this method is used to maintain the ServerConnectionLog which is given as a parameter to the constructor

        Request request = new Request(this, params, number, result);
        this.requests.add(request);
        this.log.addItem(this.id, this);
        return request;
    }
}
