package mathapp.http.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import mathapp.common.Colors;
import mathapp.common.Constants;
import mathapp.common.Logger;
import mathapp.common.MathService;
import mathapp.common.Params;
import mathapp.common.ServerBase;

// This class implements an HTTP server

public class HTTPServer implements ServerBase {

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

    // Registered handler class for named context
    static class CalcContextHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange request) throws IOException {
            Logger.server(
                Colors.ANSI_YELLOW + request.getRequestMethod() + Colors.ANSI_RESET + " " + request
                    .getRequestURI().toString());
            //set to text/html for machine to machine communication
            request.getResponseHeaders().set("Content-Type", "text/html");

            String response = "";
            // Handle request type
            if (request.getRequestMethod().equalsIgnoreCase("GET")) {
                response = handleGET(request);
                if (response.equals("")) {
                    response = "Invalid query parameters provided";
                    request.sendResponseHeaders(400, 0); // 400 bad request
                } else {
                    request.sendResponseHeaders(200, 0); // 200 Ok
                }
            } else {
                request.sendResponseHeaders(501, 0); //  501 - not implemented
            }

            // Write response and close
            request.getResponseBody().write(response.getBytes());
            request.getResponseBody().close();
        }

        // Handle a HTTP GET request
        static String handleGET(HttpExchange request) throws NumberFormatException {
            Map<String, String> queryParameters = getQueryParameters(request);

            try {
                Params params = Params.fromQueryString(queryParameters);
                String result = MathService.getResult(params);
                Logger.server(
                    params.buildString() + " (" + params.toString() + ") Result: " + result);
                return result;
            } catch (Exception ex) {
                Logger.error(ex);
                return "";
            }
        }

        // Parse request query parameters into a map
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
}
