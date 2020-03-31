package mathapp.http.client;

import java.io.*;

import org.apache.http.client.fluent.Request;

import mathapp.common.*;
import mathapp.common.ClientBase;

// this class provides the client for the HTTP server

public class HttpClient extends ClientBase {

    public HttpClient() {
        boolean running = true;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Params params;
        String data;

        while (running) {
            // getting a valid Params object from the user input
            params = getValidInput(input);

            try {
                // sending the command string to the server via a HTTP GET request
                data = Request.Get(Constants.BASE_URI + params.toQueryString())
                        .connectTimeout(1000)
                        .socketTimeout(1000)
                        .execute()
                        .returnContent()
                        .asString();

                Logger.server(data);
                if (!getYesNo(input, "Do you want to do another calculation?")) {
                    running = false;
                }
            } catch (Exception ex) {
                Logger.error(ex);
            }
        }
    }
}
