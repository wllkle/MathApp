package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.common.*;
import mathapp.socket.IOSocket;

public class Client extends ClientBase {

    public static void main(String[] args) {
        IOSocket socket;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Params params;
        String data;
        Response response;

        try {
            Logger.client("Attempting to connect to server on port " + Constants.PORT);
            socket = new IOSocket(new Socket("localhost", Constants.PORT));

            currentConnection:
            while ((data = socket.receive()) != null) {
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

                    params = getValidInput(input);
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
