package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.common.*;
import mathapp.socket.IOSocket;

// This class provides the client for both the iterative and concurrent servers

public class SocketClient extends ClientBase {

    public SocketClient() {
        IOSocket socket;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Params params;
        String data;
        Response response;

        try {
            Logger.client("Attempting to connect to server on port " + Constants.PORT);

            // Instantiates a new IOSocket using a port number in common with the server it wishes to connect to
            socket = new IOSocket(new Socket("localhost", Constants.PORT));

            currentConnection:
            while ((data = socket.receive()) != null) {
                // Loops while client is connected to server, allowing one or many calculation requests

                try {
                    response = Response.fromString(data);
                    switch (response.getType()) {
                        case RESULT:
                            if (!getYesNo(input, "Do you want to do another calculation?")) {
                                break currentConnection;
                            }
                        default:
                            break;
                        case ERROR:
                            break currentConnection;
                    }

                    // Getting a valid Params object from the user input
                    params = getValidInput(input);

                    // Sending the command string to the server
                    socket.send(params.buildString());

                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }

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
