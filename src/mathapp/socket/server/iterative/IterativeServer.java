package mathapp.socket.server.iterative;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import mathapp.common.ServerBase;
import mathapp.common.*;
import mathapp.socket.server.Request;
import mathapp.socket.server.ServerConnection;
import mathapp.socket.server.ServerConnectionLog;

// this class handles the connection request and the transaction involved in the call from a client

public class IterativeServer implements ServerBase {

    // a boolean flag to control the while loop that handles connections and their requests
    private boolean running;

    // integer values used for generating ID's for connections/requests
    private int connectionCount, requestCount;

    private ServerConnectionLog log;

    public IterativeServer() {
        this.running = true;
        this.connectionCount = 0;
        this.requestCount = 0;
        this.log = new ServerConnectionLog();
    }

    // called from mathapp.Server
    public void start() {
        Socket client;
        String data;

        try {
            // establishes port for clients to connect through
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Colors.ANSI_YELLOW + Constants.PORT + Colors.ANSI_RESET);

            ServerConnection connection;
            Request request;

            while (this.running) {
                try {
                    // waits for client to connect to server
                    client = serverSocket.accept();
                    this.connectionCount++;
                    this.requestCount = 0;

                    connection = new ServerConnection(client, this.connectionCount, this.log);

                    try {
                        Logger.server(Logger.formatId(connection.getId()) + "Client connected from " + connection.getIpAddress());
                        connection.getSocket().send(ResponseType.MESSAGE, "Connected");

                        Params params;
                        String result;

                        // while client is connected
                        while ((data = connection.getSocket().receive()) != null) {
                            try {
                                // this block gets the parameters for the calculation from the client, performs
                                // the necessary calculation and returns the necessary result back to the client

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

                        // at this point the client has disconnected from the server so the client's
                        // connection will be closed and the server will loop back round waiting for
                        // another client to connect

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
