package mathapp.socket.server.iterative;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import mathapp.Logger;
import mathapp.Colors;
import mathapp.Constants;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;
import mathapp.socket.server.ServerThread;

public class IterativeServer extends Thread {

    @Override
    public void run() {
        boolean running = true;
        int connectionCount = 0;

        ServerThread process = null;
        String failedIp;

        ServerConnectionLog log = new ServerConnectionLog();
        ServerConnection connection;

        Socket client;

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            while (running) {
                try {
                    client = serverSocket.accept();
                    PrintWriter output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

                    if (process != null && process.isRunning()) {
                        failedIp = client.getInetAddress().toString().replace("/", "");
                        Logger.server("Connection from " + Colors.ANSI_YELLOW + failedIp + Colors.ANSI_RESET + " was blocked");

                        respond(output, "ERROR-Multiple connections are not supported");

                        client.close();
                        continue;
                    }

                    connectionCount++;
                    connection = new ServerConnection(client, connectionCount, log);

                    Logger.server("Connection #" + connectionCount);
                    Logger.server("Client connected from " + connection.getIpAddress() + ", ID " + connection.getId());

                    process = new ServerThread(connection);
                    process.start();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error(ex);
                    if (ex.getClass() != SocketException.class) {
                        Logger.server(Colors.ANSI_RED + ex.getMessage() + Colors.ANSI_RESET + " " + ex.getClass().getTypeName());
                        Logger.system("Exiting");
                    }

                    if (process != null) {
                        process.interrupt();
                        process = null;
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
