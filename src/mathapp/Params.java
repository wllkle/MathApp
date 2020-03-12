package mathapp;

public class Params {

    private String operand;
    private double arg1, arg2;

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

    public Params(String operand, double arg1, double arg2) {
        this.operand = operand;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public static Params fromString(String value) {
        try {
            String[] params = value.split(":");
            return new Params(params[0], Double.parseDouble(params[1]), Double.parseDouble(params[2]));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new Params("+", 0, 0);
        }
    }

    @Override
    public String toString() {
        return String.join(" " + operand + " ", Double.toString(arg1), Double.toString(arg2));
    }
}
