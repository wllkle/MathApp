package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.common.*;
import mathapp.socket.IOSocket;

public class Client extends ClientBase {

    public static void main(String[] args) {
        boolean firstRun = true;
        IOSocket socket;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Params params;
        String data;

        try {
            Logger.client("Attempting to connect to server on port " + Constants.PORT);
            socket = new IOSocket(new Socket("localhost", Constants.PORT));

            while ((data = socket.receive()) != null) {
                if (firstRun) {
                    Response.fromString(data);
                    firstRun = false;
                }

                try {
                    params = getValidInput(input);
                    socket.send(params.buildString());

                    try {
                        Response.fromString(socket.receive());
                    } catch (Exception ex) {
                        Logger.error(ex);
                    }

                    if (!getYesNo(input, "Do you want to do another calculation?")) {
                        break;
                    }
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
