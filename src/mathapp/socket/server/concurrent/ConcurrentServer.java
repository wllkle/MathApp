package mathapp.socket.server.concurrent;

import mathapp.ServerBase;
import mathapp.common.Logger;
import mathapp.common.Colors;
import mathapp.common.Constants;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;
import mathapp.socket.server.ServerThread;

import java.net.ServerSocket;
import java.net.Socket;

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
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Concurrent server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            while (this.running) {
                try {
                    client = serverSocket.accept();
                    this.connectionCount++;

                    threadManager.closeCompleted();

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
