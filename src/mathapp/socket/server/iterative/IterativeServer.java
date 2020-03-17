package mathapp.socket.server.iterative;

import java.io.*;
import java.net.*;

import mathapp.*;
import mathapp.Constants;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;
import mathapp.socket.server.ServerThread;

public class IterativeServer extends Thread {

    @Override
    public void run() {
        boolean running = true;
        int connectionCount = 0;
        ServerThread currentClient = null;
        String failedIp;

        ServerConnectionLog log = new ServerConnectionLog();
        ServerConnection connection;

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Constants.PORT);

            while (running) {
                try (Socket client = serverSocket.accept()) {
                    connectionCount++;
                    connection = new ServerConnection(client, connectionCount, log);

                    Logger.server("Connection #" + connectionCount);
                    Logger.server(connection.getId() + " connected from " + connection.getIpAddress());

                    currentClient = new ServerThread(connection);
                    currentClient.start();

                    while (currentClient.isRunning()) {
                        try (Socket failClient = serverSocket.accept()) {
                            failedIp = failClient.getInetAddress().toString().replace("/", "");
                            Logger.server("Connection from " + Colors.ANSI_YELLOW + failedIp + Colors.ANSI_RESET + " was blocked");
                            respond(new PrintWriter(new OutputStreamWriter(failClient.getOutputStream())),
                                    "A client is already connected, please wait until they disconnect.");
                        } catch (Exception ex) {
                            Logger.error(ex);
                        }
                    }

                    Logger.server(connection.getId() + " disconnected");
                    currentClient.interrupt();
                    currentClient = null;

                } catch (Exception ex) {
                    Logger.error(ex);
                    if (ex.getClass() != SocketException.class) {
                        Logger.server(Colors.ANSI_RED + ex.getMessage() + Colors.ANSI_RESET + " " + ex.getClass().getTypeName());
                        Logger.system("Exiting");
                    }

                    if (currentClient != null) {
                        currentClient.interrupt();
                        currentClient = null;
                    }
                }
            }

        } catch (Exception ex) {
            Logger.error(ex);
        }
    }

    private void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }
}
