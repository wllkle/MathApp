package mathapp.socket.server;

import mathapp.common.Logger;
import mathapp.common.MathService;
import mathapp.common.Params;

import java.net.SocketException;

public class ServerThread extends Thread {

    private ServerConnection connection;

    public ServerThread(ServerConnection connection) {
        this.connection = connection;
    }

    public ServerConnection getConnection() {
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

            this.connection.getSocket().send("SERVER-Connected");

            while ((data = this.connection.getSocket().receive()) != null) {
                try {
                    requestCount++;
                    params = Params.fromString(data);
                    result = MathService.getResult(params);

                    request = this.connection.addRequest(params, requestCount, result);
                    Logger.worker(Logger.formatId(request.getId()) + params.buildString() + " (" + params.toString() + ") Result: " + result);
                    this.connection.getSocket().send("RESULT-" + result);

                } catch (Exception ex) {
                    if (ex.getClass() == SocketException.class) {
                        Logger.server(Logger.formatId(this.connection.getId()) + "Client disconnected");
                        break;
                    } else {
                        Logger.error(ex);
                    }
                }
            }
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
