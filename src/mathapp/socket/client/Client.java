package mathapp.socket.client;

import java.io.*;
import java.net.*;
//import java.util.HashMap;

import mathapp.Logger;
import org.apache.commons.lang3.StringUtils;

import mathapp.Params;
import mathapp.socket.Constants;

public class Client {
    public static void main(String[] args) {
        Socket socket;
        BufferedReader input, ingest;
        PrintWriter output;

        boolean running = true;
        String permittedOperands = "+-*/e";

        try {
            socket = new Socket("localhost", Constants.PORT);
            System.out.println("Connection established");

            input = new BufferedReader(new InputStreamReader(System.in));
            ingest = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            Params params = null;
            String inputText, operand, result;
            String[] splitInput;
            double arg1, arg2;

            while (running) {
                try {
                    System.out.println("Please enter an equation eg. 89 - 36.5");
                    inputText = input.readLine().trim().replaceAll(" +", " ");

                    // validate
                    if (inputText.matches(".*[a-zA-Z]+.*")) {
                        Logger.error("Alphabetical characters are not permitted");
                    }

//                    if (countChar(inputText, permittedOperands) != 1) {
//                        System.out.println("Please only provide one operand");
//                        continue;
//                    }

//                    if (countChar(inputText, " ") != 2) {
//                        System.out.println("ok" + inputText);
//                        for (int i = 0; i < inputText.length(); i++) {
//                            char c = inputText.charAt(i);
//                            System.out.println(c);
//                            if (charMatches(c, permittedOperands)) {
//                                if (inputText.charAt(i - 1) != ' ') {
//                                    inputText = insertIntoString(inputText, i, " ");
//                                }
//                                if (inputText.charAt(i + 1) != ' ') {
//                                    inputText = insertIntoString(inputText, i + 1, " ");
//                                }
//                                System.out.println(inputText);
//                                break;
//                            }
//                        }
//                    }

                    // start compute

                    splitInput = inputText.split(" ");
                    if (splitInput.length == 3) {
                        operand = splitInput[1];
                        arg1 = Double.parseDouble(splitInput[0].trim());
                        arg2 = Double.parseDouble(splitInput[2].trim());

                        if (!operand.equals("")) {
                            params = new Params(operand, arg1, arg2);
                            respond(output, params.buildString());
                            result = ingest.readLine();
                            System.out.println(result + "\n");
                        } else {
                            System.out.println("+ - * / e are valid operands\n");
                        }

                        System.out.println("Do you want to do another? y/n");
                        switch (input.readLine().toLowerCase()) {
                            case "n":
                                running = false;
                                break;
                            default:
                                System.out.println("Do you want to do another? y/n");
                            case "y":
                                break;
                        }
                    } else {
                        // not valid
                    }
                } catch (Exception ex) {
                    Logger.error(ex);
                }
            }

            output.close();
            input.close();

        } catch (Exception ex) {
            Logger.error(ex);
        }

        System.exit(1);
    }

    private static int countChar(String value, String find) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (charMatches(value.charAt(i), find)) {
                count++;
            }
        }
        System.out.println(value + " " + find + " " + count);
        return count;
    }

    private static boolean charMatches(char value, String find) {
        return StringUtils.contains(Character.toString(value), find);
    }

    private static String insertIntoString(String value, int insertAt, String insert) {
        return value.substring(0, insertAt) + insert + value.substring(insertAt, insert.length());
    }

    private static void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }
}
