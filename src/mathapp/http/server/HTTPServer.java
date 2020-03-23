package mathapp.http.server;

import mathapp.ServerBase;
import mathapp.common.Constants;
import mathapp.common.Logger;

import java.net.InetSocketAddress;
import java.util.*;
import java.io.*;

import com.sun.net.httpserver.*;
import mathapp.common.MathService;
import mathapp.common.Params;

public class HTTPServer implements ServerBase {

    // registered handler class for named context
    static class CalcContextHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange request) throws IOException {
            Logger.server(request.getRequestMethod() + " " + request.getRequestURI().toString());
            //set to text/html for machine to machine communication
            request.getResponseHeaders().set("Content-Type", "text/html");

            String response = "";
            // handle request type
            if (request.getRequestMethod().equalsIgnoreCase("GET")) {
                response = handleGET(request);
                if (response.equals("")) {
                    request.sendResponseHeaders(404, 0); // 404 bad request
                } else {
                    request.sendResponseHeaders(200, 0); // 200 Ok
                }
            } else {
                request.sendResponseHeaders(501, 0); //  501 - not implemented
            }
            // write response and close
            request.getResponseBody().write(response.getBytes());
            request.getResponseBody().close();
        }

        // handle a HTTP GET request
        static String handleGET(HttpExchange request) throws IOException, NumberFormatException {
            Map<String, String> queryParameters = getQueryParameters(request);

            try {
                Params params = Params.fromQueryString(queryParameters);
                String result = MathService.getResult(params);
                Logger.server("Result " + result);
                return result;
            } catch (Exception ex) {
                Logger.error(ex);
                return "";
            }
        }

        // parse request query parameters into a Map
        static Map<String, String> getQueryParameters(HttpExchange request) {
            Map<String, String> result = new HashMap<>();
            String query = request.getRequestURI().getQuery();
            if (query != null) {
                for (String param : query.split("&")) {
                    String pair[] = param.split("=");
                    if (pair.length > 1) {
                        result.put(pair[0], pair[1]);
                    } else {
                        result.put(pair[0], "");
                    }
                }
            }
            return result;
        }
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(Constants.PORT), 0);
            Logger.server("HTTP server started");
            server.createContext("/calc", new CalcContextHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException ex) {
            Logger.error(ex);
        }
    }
}
