package mathapp.socket.server.iterative;

import java.io.*;
import java.net.*;

import mathapp.*;
import mathapp.Constants;
import mathapp.socket.server.Request;
import mathapp.socket.server.ServerConnection;

import java.util.HashMap;

public class IterativeServer extends Thread {

    @Override
    public void run() {

        boolean running = true;
        String data;
        int connectionCount = 0, requestCount = 0;

        ServerConnection connection;
        Request request;
        HashMap<String, ServerConnection> connectionLog = new HashMap<>();

        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            Logger.server("Iterative server listening on port " + Constants.PORT);

            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    connectionCount++;

                    connection = new ServerConnection(client, connectionCount, connectionLog);

                    Logger.server("Connection #" + connectionCount);
                    Logger.server(connection.getId() + " connected with IP " + connection.getIpAddress());

                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

                    Object result;
                    Params params;
                    double[] args;

                    while ((data = input.readLine()) != null) {
                        try {
                            requestCount++;
                            Logger.server("Request #" + requestCount);
                            Logger.server("Ingest " + Colors.ANSI_YELLOW + data + Colors.ANSI_RESET);
                            params = Params.fromString(data);
                            args = params.getArgs();

                            switch (params.getOperand()) {
                                case "+":
                                    result = MathService.add(args[0], args[1]);
                                    break;
                                case "-":
                                    result = MathService.sub(args[0], args[1]);
                                    break;
                                case "*":
                                    result = MathService.mul(args[0], args[1]);
                                    break;
                                case "/":
                                    result = MathService.div(args[0], args[1]);
                                    break;
                                case "^":
                                    result = MathService.exp(args[0], args[1]);
                                    break;
                                default:
                                    result = 0;
                                    break;
                            }

                            request = connection.addRequest(params, requestCount, result);

                            Logger.server(request.getId() + " - " + params.toString() + " (Result " + result + ")");

                            respond(output, result.toString());
                        } catch (Exception ex) {
                            Logger.error(ex);
                        }
                    }
                    Logger.server(connection.getId() + " disconnected");
                    client.close();
                    requestCount = 0;
                } catch (Exception ex) {
                    Logger.error(ex);
                    if (ex.getClass() != SocketException.class) {
                        Logger.server(Colors.ANSI_RED + ex.getMessage() + Colors.ANSI_RESET + " " + ex.getClass().getTypeName());
                        Logger.system("Exiting");
                    }
                }
            }
            serverSocket.close();
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }
}
