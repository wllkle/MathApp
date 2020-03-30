package mathapp.socket.server.concurrent;

import mathapp.ServerBase;
import mathapp.common.Logger;
import mathapp.common.Colors;
import mathapp.common.Constants;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;

import java.net.ServerSocket;
import java.net.Socket;

// this class handles the connection request and starts a ServerThread for each connection

public class ConcurrentServer implements ServerBase {

    private boolean running;
    private int connectionCount;
    private ServerConnectionLog log;
    private ThreadManager threadManager;

    public ConcurrentServer() {
        this.running = true;
        this.connectionCount = 0;
        this.log = new ServerConnectionLog();
        this.threadManager = new ThreadManager();
    }

    public void start() {
        ServerConnection connection;
        Socket client;

        try {
            // establishes port for clients to connect through
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);

            Logger.server("Concurrent server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            while (this.running) {
                try {
                    // waits for client to connect to server
                    client = serverSocket.accept();
                    this.connectionCount++;

                    this.threadManager.closeCompleted();

                    connection = new ServerConnection(client, this.connectionCount, this.log);
                    Logger.server(Logger.formatId(connection.getId()) + "Client connected from " + connection.getIpAddress());

                    this.threadManager.addThread(new ServerThread(connection));
                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
}
