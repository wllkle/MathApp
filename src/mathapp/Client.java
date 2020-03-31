package mathapp;

import java.util.Scanner;
import mathapp.common.Logger;
import mathapp.http.client.HttpClient;
import mathapp.socket.client.SocketClient;

// this is the entry-point for client execution, here a decision is made on which type of client to run

public class Client {

    public static void main(String[] args) {
        boolean acceptedValue = false;
        String input;

        Scanner scanner = new Scanner(System.in);

        Logger.system("Which type of client do you wish to run?");
        Logger.system("[1] Socket");
        Logger.system("[2] HTTP");

        while (!acceptedValue) {
            Logger.input();
            input = scanner.nextLine();
            if (input.length() > 0) {
                switch (input.substring(0, 1)) {
                    case "1":
                        acceptedValue = true;
                        new SocketClient();
                        break;
                    case "2":
                        acceptedValue = true;
                        new HttpClient();
                        break;
                    default:
                        acceptedValue = false;
                        break;
                }
            }
        }

        Logger.blank();
    }
}
