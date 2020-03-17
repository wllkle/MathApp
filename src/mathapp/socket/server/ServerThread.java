package mathapp.socket.server;

import mathapp.Colors;
import mathapp.Logger;
import mathapp.MathService;
import mathapp.Params;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {

    private Socket socket;
    private ServerConnection connection;
    private boolean running;

    public ServerThread(ServerConnection connection) {
        this.socket = connection.getSocket();
        this.connection = connection;
        this.running = true;
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void run() {
        int requestCount = 0;
        String data;
        Request request;

        try (BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter output = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()))) {

            Object result;
            Params params;
            double[] args;

            while ((data = input.readLine()) != null) {
                try {
                    requestCount++;
                    Logger.server("Request #" + requestCount);
                    Logger.server("Ingest " + Colors.ANSI_YELLOW + data + Colors.ANSI_RESET);
                    params = Params.fromString(data);
                    args = params.getArgs();

                    switch (params.getOperand()) {
                        case "+":
                            result = MathService.add(args[0], args[1]);
                            break;
                        case "-":
                            result = MathService.sub(args[0], args[1]);
                            break;
                        case "*":
                            result = MathService.mul(args[0], args[1]);
                            break;
                        case "/":
                            result = MathService.div(args[0], args[1]);
                            break;
                        case "^":
                            result = MathService.exp(args[0], args[1]);
                            break;
                        default:
                            result = 0;
                            break;
                    }

                    request = this.connection.addRequest(params, requestCount, result);

                    Logger.server(request.getId() + " - " + params.toString() + " (Result " + result + ")");
                    respond(output, result.toString());

                } catch (Exception ex) {
                    Logger.error(ex);
                    if (ex.getClass() == SocketException.class) {
                        this.running = false;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.error(ex);
            this.running = false;
        }
    }

    private void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }
}
