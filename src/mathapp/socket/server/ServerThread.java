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

    public ServerConnection getConnection() {
        return this.connection;
    }

    @Override
    public void run() {
        int requestCount = 0;
        String data;
        Request request;

        try (BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter output = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()))) {

            respond(output, "SERVER-Connection successful");
            Logger.worker(Logger.formatId(this.connection.getId()) + "Worker thread started");

            Params params;
            String result;

            while ((data = input.readLine()) != null) {
                try {
                    requestCount++;
                    params = Params.fromString(data);

                    result = MathService.getResult(params);

                    request = this.connection.addRequest(params, requestCount, result);
                    Logger.worker(Logger.formatId(request.getId()) + params.buildString() + " (" + params.toString() + ") Result: " + result);
                    respond(output, "RESULT-" + result);

                } catch (Exception ex) {
                    if (ex.getClass() == SocketException.class) {
                        Logger.server(Logger.formatId(this.connection.getId()) + "Client disconnected");
                        this.running = false;
                    } else {
                        Logger.error(ex);
                    }
                }
            }

            this.running = false;
        } catch (Exception ex) {
            if (ex.getClass() == SocketException.class) {
                Logger.server(Logger.formatId(this.connection.getId()) + "Client disconnected");
            } else {
                Logger.error(ex);
            }
            this.running = false;
        }
    }

    private void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }
}
