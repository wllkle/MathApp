package mathapp.socket.server.concurrent;

import mathapp.Logger;
import mathapp.Colors;
import mathapp.Constants;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;
import mathapp.socket.server.ServerThread;

import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentServer extends Thread {

    @Override
    public void run() {
        boolean running = true;
        int connectionCount = 0;

        ThreadManager threadManager = new ThreadManager();

        ServerConnectionLog log = new ServerConnectionLog();
        ServerConnection connection;

        Socket client;

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Concurrent server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            while (running) {
                try {
                    client = serverSocket.accept();
                    connectionCount++;

                    threadManager.closeCompleted();

                    connection = new ServerConnection(client, connectionCount, log);
                    Logger.server(Logger.formatId(connection.getId()) + "Client connected from " + connection.getIpAddress());

                    threadManager.addThread(new ServerThread(connection));

                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }
}
