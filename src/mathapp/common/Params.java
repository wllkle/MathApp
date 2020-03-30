package mathapp.common;

import java.util.Map;

// this class manages parameters for the maths calculation, involving one operand and two arguments,
// it builds builds the calculation string in the format required to be communicated from client
// and server

public class Params {

    private String operand;
    private double arg1, arg2;

    Params(String operand, double arg1, double arg2) {
        this.operand = operand;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getOperand() {
        return operand;
    }

    public double[] getArgs() {
        double[] args = new double[2];
        args[0] = arg1;
        args[1] = arg2;
        return args;
    }

    // creates the calculation as a string in the format required by the server
    public String buildString() {
        return String.join(":", operand, Double.toString(arg1), Double.toString(arg2));
    }

    // creates the calculation as a query string required by the HTTP server
    public String toQueryString() {
        String safeOperand;
        switch (this.operand) {
            default:
            case "+":
                safeOperand = "a";
                break;
            case "-":
                safeOperand = "s";
                break;
            case "*":
                safeOperand = "m";
                break;
            case "/":
                safeOperand = "d";
                break;
            case "^":
                safeOperand = "e";
                break;
        }
        return "?arg1=" + arg1 + "&arg2=" + arg2 + "&operand=" + safeOperand;
    }

    // presents calculation in a human-readable format
    @Override
    public String toString() {
        return Colors.ANSI_YELLOW + String.join(" " + operand + " ", Double.toString(arg1), Double.toString(arg2)) + Colors.ANSI_RESET;
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
        String operandValue;
        double value1, value2;
        try {
            operandValue = queryParameters.get("operand");
            value1 = Double.parseDouble(queryParameters.get("arg1"));
            value2 = Double.parseDouble(queryParameters.get("arg2"));

            if (operandValue.length() > 0) {
                switch (operandValue.substring(0, 1)) {
                    case "a":
                        operandValue = "+";
                        break;
                    case "s":
                        operandValue = "-";
                        break;
                    case "m":
                        operandValue = "*";
                        break;
                    case "d":
                        operandValue = "/";
                        break;
                    case "e":
                        operandValue = "^";
                        break;
                    default:
                        throw new Exception();
                }
            } else {
                throw new Exception();
            }

            return new Params(operandValue, value1, value2);

        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid query parameters provided");
        }
    }
}
