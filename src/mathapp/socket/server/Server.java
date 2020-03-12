package mathapp.socket.server;

import mathapp.Logger;
import mathapp.socket.server.iterative.IterativeServer;

import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Thread thread;
        Scanner scanner = new Scanner(System.in);

        Logger.system("Which type of server do you wish to run?");
        Logger.system("[1]  Iterative");
        Logger.system("[2]  Concurrent");
        Logger.input();

        if (scanner.next().equals("1")) {
            thread = new IterativeServer();
            Logger.system("Starting Iterative server");
        } else {
            thread = new IterativeServer();
            Logger.system("Starting Concurrent server");
        }

        thread.start();
    }
}
