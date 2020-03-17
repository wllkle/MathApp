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
            case APP:
                color = Colors.ANSI_YELLOW;
                break;
            case QUEUE:
                color = Colors.ANSI_PURPLE;
                break;
            case QUEUEMGR:
                color = Colors.ANSI_CYAN;
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

        return time + _type + padding + message;
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

    public static void queue(String message) {
        queue(message, true);
    }

    public static void queue(String message, boolean line) {
        print(log(LogType.QUEUE, message), line);
    }

    public static void queueM(String message) {
        queueM(message, true);
    }

    public static void queueM(String message, boolean line) {
        print(log(LogType.QUEUEMGR, message), line);
    }

    public static void error(String message) {
        print(log(LogType.ERROR, message), true);
    }

    public static void error(Exception ex) {
        ex.printStackTrace();
        String msg = ex.getMessage();
        try {
            msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
        } catch (Exception e) {
            msg = "An error occurred";
        }
        print(log(LogType.ERROR, msg), true);
    }
}

enum LogType {SYSTEM, APP, SERVER, QUEUE, QUEUEMGR, ERROR}
