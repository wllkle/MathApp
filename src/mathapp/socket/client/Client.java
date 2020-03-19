package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.*;

public class Client extends ClientBase {

    public static void main(String[] args) {
        boolean running = true;
        Socket socket;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in)), ingest;
        PrintWriter output;
        Params params;

        try {
            Logger.client("Attempting to connect to server on port " + Constants.PORT);
            socket = new Socket("localhost", Constants.PORT);
            ingest = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Response.fromString(ingest.readLine());
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
