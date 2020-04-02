package mathapp.common;

import java.util.Map;

// this class manages parameters for the maths calculation, involving one operator and two arguments,
// it builds builds the calculation string in the format required to be communicated from client
// and server

public class Params {

    private String operator;
    private double arg1, arg2;

    Params(String operator, double arg1, double arg2) {
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getOperator() {
        return operator;
    }

    public double[] getArgs() {
        double[] args = new double[2];
        args[0] = arg1;
        args[1] = arg2;
        return args;
    }

    // creates the calculation as a string in the format required by the server
    public String buildString() {
        return String.join(":", operator, Double.toString(arg1), Double.toString(arg2));
    }

    // creates the calculation as a query string required by the HTTP server
    public String toQueryString() {
        String safeOperator;
        switch (this.operator) {
            default:
            case "+":
                safeOperator = "a";
                break;
            case "-":
                safeOperator = "s";
                break;
            case "*":
                safeOperator = "m";
                break;
            case "/":
                safeOperator = "d";
                break;
            case "^":
                safeOperator = "e";
                break;
        }
        return "?arg1=" + arg1 + "&arg2=" + arg2 + "&operator=" + safeOperator;
    }

    // presents calculation in a human-readable format
    @Override
    public String toString() {
        return Colors.ANSI_YELLOW + String.join(" " + operator + " ", Double.toString(arg1), Double.toString(arg2)) + Colors.ANSI_RESET;
    }

    // method decomposes received string by the server into a Params object
    public static Params fromString(String value) throws IllegalArgumentException {
        try {
            String[] params = value.split(":");
            if (params.length != 3) {
                throw new Exception();
            }
            return new Params(params[0], Double.parseDouble(params[1]), Double.parseDouble(params[2]));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Value: " + value + " Error" + ex.getMessage());
        }
    }

    // method decomposes a map of query string parameters into a Params object
    public static Params fromQueryString(Map<String, String> queryParameters) throws IllegalArgumentException {
        String operatorValue;
        double value1, value2;
        try {
            operatorValue = queryParameters.get("operator");
            value1 = Double.parseDouble(queryParameters.get("arg1"));
            value2 = Double.parseDouble(queryParameters.get("arg2"));

            if (operatorValue.length() > 0) {
                switch (operatorValue.substring(0, 1)) {
                    case "a":
                        operatorValue = "+";
                        break;
                    case "s":
                        operatorValue = "-";
                        break;
                    case "m":
                        operatorValue = "*";
                        break;
                    case "d":
                        operatorValue = "/";
                        break;
                    case "e":
                        operatorValue = "^";
                        break;
                    default:
                        throw new Exception();
                }
            } else {
                throw new Exception();
            }

            return new Params(operatorValue, value1, value2);

        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid query parameters provided");
        }
    }
}
