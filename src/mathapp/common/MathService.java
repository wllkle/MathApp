package mathapp.common;

// MathService is used by all three server types, the getResult method is supplied with a Params
// object and it returns the result as a string

public class MathService {
    private static double add(double a, double b) {
        return a + b;
    }
    private static double sub(double a, double b) {
        return a - b;
    }
    private static double mul(double a, double b) {
        return a * b;
    }
    private static double div(double a, double b) {
        return a / b;
    }
    private static double exp(double a, double b) {
        try {
            return Math.pow(a, b);
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            return 0;
        }
    }

    public static String getResult(Params params) {
        double result;
        double[] args = params.getArgs();
        switch (params.getOperator()) {
            case "+":
                result = MathService.add(args[0], args[1]);
                break;
            case "-":
                result = MathService.sub(args[0], args[1]);
                break;
            case "*":
                result = MathService.mul(args[0], args[1]);
                break;
            case "/":
                result = MathService.div(args[0], args[1]);
                break;
            case "^":
                result = MathService.exp(args[0], args[1]);
                break;
            default:
                return "";
        }
        return Double.toString(result);
    }
}
