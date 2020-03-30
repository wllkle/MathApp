package mathapp;

import mathapp.common.Logger;
import mathapp.http.server.HTTPServer;
import mathapp.socket.server.iterative.IterativeServer;
import mathapp.socket.server.concurrent.ConcurrentServer;

import java.util.Scanner;

// this is the entry-point for server execution, here a decision is made on which type of server to run

public class Server {
    private static ServerBase server;

    public static void main(String[] args) {
        boolean acceptedValue = false;
        String input;

        Scanner scanner = new Scanner(System.in);

        Logger.system("Which type of server do you wish to run?");
        Logger.system("[1] Iterative");
        Logger.system("[2] Concurrent");
        Logger.system("[3] HTTP");

        while (!acceptedValue) {
            Logger.input();
            input = scanner.nextLine();
            if (input.length() > 0) {
                switch (input.substring(0, 1)) {
                    case "1":
                        setServer(new IterativeServer());
                        break;
                    case "2":
                        setServer(new ConcurrentServer());
                        break;
                    case "3":
                        setServer(new HTTPServer());
                        break;
                    default:
                        server = null;
                        break;
                }
            }

            if (server != null) {
                acceptedValue = true;
            }
        }

        Logger.blank();
        server.start();
    }

    private static void setServer(Object serverObj) {
        try {
            server = (ServerBase) serverObj;
        } catch (Exception ex) {
            server = null;
            Logger.error(ex);
        }
    }
}
