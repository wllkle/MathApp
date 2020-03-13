package mathapp;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientBase {
    protected static Params getValidInput(BufferedReader input) {
        Params params = null;
        String test, permittedOperands = "+-*/^";
        String[] testElements;
        double arg1, arg2;
        boolean error;
        int operandIndex;

        Logger.app("Please enter an equation eg. 89 - 36.5");

        while (params == null) {
            error = false;
            operandIndex = -1;

            Logger.input();

            try {
                test = input.readLine().trim().replaceAll(" +", "");

                // validate

                // if input contains characters
                if (test.matches(".*[a-zA-Z]+.*")) {
                    Logger.error("Alphabetical characters are not permitted");
                    continue;
                }

                // find index of operand
                for (int i = 0; i < test.length(); i++) {
                    for (char c : permittedOperands.toCharArray()) {
                        if (test.charAt(i) == c) {
                            if (operandIndex == -1) {
                                operandIndex = i;
                            } else {
                                Logger.error("Equation invalid, please provide one operand");
                                error = true;
                                break;
                            }
                        }
                    }
                    if (error || (i == test.length() - 1 && operandIndex == -1)) {
                        if (!error) {
                            Logger.error("No valid operand found, valid operands include '+', '-', '*', '/', '^'");
                            error = true;
                        }
                        break;
                    }
                }
                if (error) {
                    continue;
                }

                if (operandIndex != -1) {
                    if (operandIndex == 0 || operandIndex == test.length() - 1) {
                        Logger.error("Somethings not quite right");
                        continue;
                    }

                    if (test.charAt(operandIndex + 1) != ' ') {
                        test = insertString(test, " ", operandIndex + 1);
                    }
                    if (test.charAt(operandIndex - 1) != ' ') {
                        test = insertString(test, " ", operandIndex);
                    }
                } else {
                    Logger.error("No valid operand found, valid operands include '+', '-', '*', '/', '^'");
                    continue;
                }

                testElements = test.split(" ");
                if (testElements.length == 3) {
                    arg1 = Double.parseDouble(testElements[0]);
                    arg2 = Double.parseDouble(testElements[2]);

                    params = new Params(testElements[1], arg1, arg2);
                }

            } catch (Exception ex) {
                Logger.error(ex);
                params = null;
            }
        }

        return params;
    }

    protected static boolean getYesNo(BufferedReader input, String message) {
        boolean valueAcquired = false, value = false;
        Logger.app(message + " y/n");

        while (!valueAcquired) {
            Logger.input();
            try {
                switch (input.readLine().toLowerCase().charAt(0)) {
                    case 'y':
                        value = true;
                        valueAcquired = true;
                        break;
                    case 'n':
                        value = false;
                        valueAcquired = true;
                        break;
                    default:
                        valueAcquired = false;
                        break;
                }
            } catch (Exception ex) {
                Logger.error(ex);
            }
        }

        return value;
    }

    protected static void respond(PrintWriter pw, String data) {
        pw.println(data);
        pw.flush();
    }

    private static String insertString(String originalString, String stringToBeInserted, int index) {
        return new StringBuilder(originalString).insert(index, stringToBeInserted).toString();
    }
}
