package mathapp.http.client;

import java.io.*;

import org.apache.http.client.fluent.Request;

import mathapp.common.*;
import mathapp.common.ClientBase;

public class Client extends ClientBase {

    public static void main(String[] args) {
        boolean running = true;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Params params;
        String data;

        while (running) {
            params = getValidInput(input);
            try {
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
