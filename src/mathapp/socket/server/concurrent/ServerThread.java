package mathapp.socket.server.concurrent;

import java.net.SocketException;

import mathapp.common.Logger;
import mathapp.common.MathService;
import mathapp.common.Params;
import mathapp.common.ResponseType;
import mathapp.socket.server.Request;
import mathapp.socket.server.ServerConnection;

// An instance of this class is created to service each client connection

public class ServerThread extends Thread {

    private ServerConnection connection;

    ServerThread(ServerConnection connection) {
        this.connection = connection;
    }

    ServerConnection getConnection() {
        return this.connection;
    }

    @Override
    public void run() {
        int requestCount = 0;
        String data;
        Request request;

        try {
            Logger.worker(Logger.formatId(this.connection.getId()) + "Worker thread started");

            Params params;
            String result;

            this.connection.getSocket().send(ResponseType.MESSAGE, "Connected");

            // While client is connected
            while ((data = this.connection.getSocket().receive()) != null) {
                try {
                    // This block gets the parameters for the calculation from the client, performs
                    // the necessary calculation and returns the necessary result back to the client

                    requestCount++;
                    params = Params.fromString(data);
                    result = MathService.getResult(params);

                    request = this.connection.addRequest(params, requestCount, result);
                    Logger.worker(
                        Logger.formatId(request.getId()) + params.buildString() + " (" + params
                            .toString() + ") Result: " + result);
                    this.connection.getSocket().send(ResponseType.RESULT, result);

                } catch (Exception ex) {
                    if (ex.getClass() == SocketException.class) {
                        break;
                    } else {
                        Logger.error(ex);
                    }
                }
            }

            Logger.server(Logger.formatId(this.connection.getId()) + "Client disconnected");

        } catch (Exception ex) {
            if (ex.getClass() == SocketException.class) {
                Logger.server(Logger.formatId(this.connection.getId()) + "Client disconnected");
            } else {
                Logger.error(ex);
            }
        }
        this.interrupt();
    }
}
