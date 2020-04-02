package mathapp.common;

import java.io.BufferedReader;

// this class contain generic methods for managing and validating user input

public class ClientBase {

    // method attempts to obtain a valid calculation command from the user
    protected static Params getValidInput(BufferedReader input) {

        Params params = null;
        String test, permittedOperators = "+-*/^";
        String[] testElements;
        double arg1, arg2;
        boolean error;
        int operatorIndex;

        Logger.client("Please enter a calculation eg. 89 - 36.5");

        while (params == null) {
            error = false;
            operatorIndex = -1;

            Logger.input();

            try {
                test = input.readLine().trim().replaceAll(" +", "");

                // validate

                // if input contains characters
                if (test.matches(".*[a-zA-Z]+.*")) {
                    Logger.error("Alphabetical characters are not permitted");
                    continue;
                }

                // find index of operator
                for (int i = 0; i < test.length(); i++) {
                    for (char c : permittedOperators.toCharArray()) {
                        if (test.charAt(i) == c) {
                            if (operatorIndex == -1) {
                                operatorIndex = i;
                            } else {
                                Logger.error("Equation invalid, please provide one operator");
                                error = true;
                                break;
                            }
                        }
                    }
                    if (error || (i == test.length() - 1 && operatorIndex == -1)) {
                        if (!error) {
                            Logger.error("No valid operator found, valid operators include '+', '-', '*', '/', '^'");
                            error = true;
                        }
                        break;
                    }
                }
                if (error) {
                    continue;
                }

                if (operatorIndex != -1) {
                    if (operatorIndex == 0 || operatorIndex == test.length() - 1) {
                        Logger.error("Something's not quite right");
                        continue;
                    }

                    if (test.charAt(operatorIndex + 1) != ' ') {
                        test = insertString(test, " ", operatorIndex + 1);
                    }
                    if (test.charAt(operatorIndex - 1) != ' ') {
                        test = insertString(test, " ", operatorIndex);
                    }
                } else {
                    Logger.error("No valid operator found, valid operators include '+', '-', '*', '/', '^'");
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

    // method attempts to obtain a yes/no response from user
    protected static boolean getYesNo(BufferedReader input, String message) {
        boolean valueAcquired = false, value = false;
        Logger.client(message + " y/n");

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

    private static String insertString(String originalString, String stringToBeInserted, int index) {
        return new StringBuilder(originalString).insert(index, stringToBeInserted).toString();
    }
}
