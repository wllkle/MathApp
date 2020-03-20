package mathapp;

import mathapp.common.Logger;
import mathapp.socket.server.iterative.IterativeServer;
import mathapp.socket.server.concurrent.ConcurrentServer;

import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Thread thread = null;
        boolean acceptedValue = false;
        String input;

        Scanner scanner = new Scanner(System.in);

        Logger.system("Which type of server do you wish to run?");
        Logger.system("[1] Iterative");
        Logger.system("[2] Concurrent");
        Logger.system("[3] HTTP");
        Logger.input();


        while (!acceptedValue) {
            input = scanner.next();
            if (input.length() > 0) {
                switch (input.substring(0, 1)) {
                    case "1":
                        new IterativeServer();
                        break;
                    case "2":
                        new ConcurrentServer();
                        break;
                    case "3":

                        break;
                    default:
                        Logger.input();
                        break;
                }
            }

            if (thread != null) {
                acceptedValue = true;
            }
        }

        Logger.blank();
        thread.start();
    }
}
