package mathapp.common;

import java.io.InvalidObjectException;
import java.util.Base64;

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

    public String buildString() {
        return String.join(":", operand, Double.toString(arg1), Double.toString(arg2));
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(this.buildString().getBytes());
    }

    public static Params fromBase64(String value) throws IllegalArgumentException {
        try {
            return fromString(Base64.getDecoder().decode(value).toString());
        } catch (Exception ex) {
            Logger.error(ex);
            throw new IllegalArgumentException("Invalid value");
        }
    }

    @Override
    public String toString() {
        return Colors.ANSI_YELLOW + String.join(" " + operand + " ", Double.toString(arg1), Double.toString(arg2)) + Colors.ANSI_RESET;
    }

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
}