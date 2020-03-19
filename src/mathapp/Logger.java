package mathapp;

import java.time.LocalTime;

public class Logger {

    private static String log(LogType type, String message) {
        String color = Colors.ANSI_RESET;
        String _type = type.name();
        String padding = "";
        int paddingLength = 10 - _type.length();

        for (int i = 0; i < paddingLength; i++)
            padding += ' ';

        switch (type) {
            case SYSTEM:
                color = Colors.ANSI_GREEN;
                break;
            case SERVER:
                color = Colors.ANSI_BLUE;
                break;
            case CLIENT:
                color = Colors.ANSI_YELLOW;
                break;
            case WORKER:
            case APP:
                color = Colors.ANSI_PURPLE;
                break;
            case ERROR:
                color = Colors.ANSI_RED;
                break;
        }

        _type = Colors.ANSI_RESET + "[" + color + _type + Colors.ANSI_RESET + "]";
        String currentTime = LocalTime.now().toString();
        while (currentTime.length() != 18)
            currentTime = currentTime.concat("0");

        String time = "[" + Colors.ANSI_GREEN + currentTime + Colors.ANSI_RESET + "]  ";

        return time + _type + padding + message + Colors.ANSI_RESET;
    }

    private static void print(String message, boolean line) {
        if (line) {
            System.out.println(message);
        } else {
            System.out.print(message);
        }
    }

    public static void blank() {
        System.out.println();
    }

    public static void input() {
        System.out.print("\t\t\t\t\t\t\t  " + Colors.ANSI_BLUE + ">" + Colors.ANSI_YELLOW + ">" + Colors.ANSI_RESET + "> ");
    }

    public static void system(String message) {
        system(message, true);
    }

    public static void system(String message, boolean line) {
        print(log(LogType.SYSTEM, message), line);
    }

    public static void app(String message) {
        app(message, true);
    }

    public static void app(String message, boolean line) {
        print(log(LogType.APP, message), line);
    }

    public static void server(String message) {
        server(message, true);
    }

    public static void server(String message, boolean line) {
        print(log(LogType.SERVER, message), line);
    }

    public static void worker(String message) {
        worker(message, true);
    }

    public static void worker(String message, boolean line) {
        print(log(LogType.WORKER, message), line);
    }

    public static void client(String message) {
        client(message, true);
    }

    public static void client(String message, boolean line) {
        print(log(LogType.CLIENT, message), line);
    }

    public static void error(String message) {
        print(log(LogType.ERROR, message), true);
    }

    public static void error(Exception ex) {
//        ex.printStackTrace();
        String msg = ex.getMessage();
        try {
            msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
        } catch (Exception e) {
            msg = "An error occurred";
        }
        print(log(LogType.ERROR, msg), true);
    }

    public static String formatId(String value) {
        return "[" + Colors.ANSI_BLUE + value + Colors.ANSI_RESET + "]";
    }
}

enum LogType {SYSTEM, APP, SERVER, WORKER, CLIENT, ERROR}
