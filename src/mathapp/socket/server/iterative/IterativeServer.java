package mathapp.socket.server.iterative;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import mathapp.*;
import mathapp.socket.server.Request;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;

public class IterativeServer extends Thread {

    @Override
    public void run() {
        boolean running = true;
        int connectionCount = 0, requestCount = 0;

        ServerConnectionLog log = new ServerConnectionLog();
        ServerConnection connection;

        Socket client;

        String data;
        Request request;

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            while (running) {
                try {
                    client = serverSocket.accept();
                    connectionCount++;

                    connection = new ServerConnection(client, connectionCount, log);

                    try (BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                         PrintWriter output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()))) {

                        respond(output, "SERVER-Connection successful");
                        Logger.server(Logger.formatId(connection.getId()) + "Client connected from " + connection.getIpAddress());

                        Params params;
                        String result;

                        while ((data = input.readLine()) != null) {
                            try {
                                requestCount++;
                                params = Params.fromString(data);

                                result = MathService.getResult(params);

                                request = connection.addRequest(params, requestCount, result);
                                Logger.worker(Logger.formatId(request.getId()) + params.buildString() + " (" + params.toString() + ") Result: " + result);
                                respond(output, "RESULT-" + result);

                            } catch (Exception ex) {
                                if (ex.getClass() == SocketException.class) {
                                    Logger.server(Logger.formatId(connection.getId()) + "Client disconnected");
                                } else {
                                    Logger.error(ex);
                                }
                            }
                        }

                        Logger.server(Logger.formatId(connection.getId()) + "Client disconnected");
                        client.close();
                    } catch (Exception ex) {
                        if (ex.getClass() == SocketException.class) {
                            Logger.server(Logger.formatId(connection.getId()) + "Client disconnected");
                        } else {
                            Logger.error(ex);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error(ex);
                    if (ex.getClass() != SocketException.class) {
                        Logger.server(Colors.ANSI_RED + ex.getMessage() + Colors.ANSI_RESET + " " + ex.getClass().getTypeName());
                        Logger.system("Exiting");
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
