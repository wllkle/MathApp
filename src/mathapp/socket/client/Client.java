package mathapp.socket.client;

import java.io.*;
import java.net.*;

import mathapp.ClientBase;
import mathapp.Colors;
import mathapp.Logger;
import mathapp.Params;
import mathapp.Constants;

public class Client extends ClientBase {
    public static void main(String[] args) {
        Socket socket;
        BufferedReader input, ingest;
        PrintWriter output;

        boolean running = true;

        try {
            socket = new Socket("localhost", Constants.PORT);
            Logger.server("Connection established");

            input = new BufferedReader(new InputStreamReader(System.in));
            ingest = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // if (ingestRead == null) {
            //  running = false;
            // Logger.error("error happened lol");
            // throw new Exception("ex");
            // }

            Params params;
            String result;

            while (running) {
                try {
                    params = getValidInput(input);
                    respond(output, params.buildString());
                    result = ingest.readLine();
                    Logger.server("Result " + Colors.ANSI_YELLOW + result + Colors.ANSI_RESET);

                    if (!getYesNo(input, "Do you want to do another calculation?")) {
                        running = false;
                    }
                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }

            output.close();
            input.close();

        } catch (Exception ex) {
            Logger.error(ex);
        }

        System.exit(1);
    }
}
