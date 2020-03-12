package mathapp;

public class MathService {

    public static double add(double a, double b) {
        return a + b;
    }
    public static double sub(double a, double b) {
        return a - b;
    }
    public static double mul(double a, double b) {
        return a * b;
    }
    public static double div(double a, double b) {
        return a / b;
    }
    public static double exp(double a, double b) {
        try {
            return Math.pow(a, b);
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            return 0;
        }
    }
}
