package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.*;

public class Client extends ClientBase {

    public static void main(String[] args) {
        boolean running = true;
        int connectionAttempt = 0;
        Socket socket = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in)), ingest = null;
        PrintWriter output;
        Params params;

        try {
            while (socket == null) {
                try {
                    connectionAttempt++;
                    Logger.client("Attempting to connect to server on port " + Constants.PORT + ", attempt #" + connectionAttempt);
                    socket = new Socket("localhost", Constants.PORT);
                    ingest = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    Response response = Response.fromString(ingest.readLine());
                    if (response.getType().equals("ERROR")) {
                        throw new IllegalAccessException();
                    }

                } catch (Exception ex) {
                    if (ex.getClass() != IllegalAccessException.class) {
                        Logger.error(ex);
                    }

                    Logger.client("Connection refused, attempts " + connectionAttempt);
                    Thread.sleep(2000);
                    socket = null;

                    if (connectionAttempt == MAX_ATTEMPTS) {
                        running = false;
                        Logger.client("Max connection attempts, exiting");
                        System.exit(1);
                        break;
                    }
                }
            }

            Logger.client("Connection established");

            ingest = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (running) {
                try {
                    params = getValidInput(input);
                    respond(output, params.buildString());

                    try {
                        Response.fromString(ingest.readLine());
                    } catch (Exception ex) {
                        Logger.error(ex);
                    }

                    if (!getYesNo(input, "Do you want to do another calculation?")) {
                        running = false;
                    }
                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }

            output.close();
            input.close();

            socket.close();
            Logger.client("Connection closed");
        } catch (Exception ex) {
            Logger.error(ex);
        }

        Logger.client("Client closing");
        System.exit(1);
    }
}
