package mathapp.socket.server.iterative;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import mathapp.ServerBase;
import mathapp.common.*;
import mathapp.socket.server.Request;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;

public class IterativeServer implements ServerBase {

    private boolean running;
    private int connectionCount, requestCount;
    private ServerConnectionLog log;

    public IterativeServer() {
        this.running = true;
        this.connectionCount = 0;
        this.requestCount = 0;
        this.log = new ServerConnectionLog();
    }

    public void start() {
        Socket client;
        String data;

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            ServerConnection connection;
            Request request;

            while (this.running) {
                try {
                    client = serverSocket.accept();
                    this.connectionCount++;
                    this.requestCount = 0;

                    connection = new ServerConnection(client, this.connectionCount, this.log);

                    try {
                        Logger.server(Logger.formatId(connection.getId()) + "Client connected from " + connection.getIpAddress());
                        connection.getSocket().send(ResponseType.MESSAGE, "Connected");

                        Params params;
                        String result;

                        while ((data = connection.getSocket().receive()) != null) {
                            try {
                                this.requestCount++;
                                params = Params.fromString(data);
                                result = MathService.getResult(params);

                                request = connection.addRequest(params, this.requestCount, result);
                                Logger.server(Logger.formatId(request.getId()) + params.buildString() + " (" + params.toString() + ") Result: " + result);
                                connection.getSocket().send(ResponseType.RESULT, result);

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
}
