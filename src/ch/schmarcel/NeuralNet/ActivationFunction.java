package ch.schmarcel.NeuralNet;

import java.util.function.Function;

public enum ActivationFunction {
    SIGMOID(x -> 1 / (1 + Math.exp(-x)), x -> x * (1 - x)),
    TANH(Math::tanh, x -> 1 - (x * x));

    public Function<Double, Double> f;
    public Function<Double, Double> df;

    ActivationFunction(Function<Double, Double> f, Function<Double, Double> df) {
        this.f = f;
        this.df = df;
    }

    public static ActivationFunction forString(String name) {
        switch (name) {
            case "sigmoid":
                return SIGMOID;
            case "tanh":
                return TANH;
            default:
                throw new IllegalArgumentException("Unknown Function: " + name);
        }
    }

    public static String asString(ActivationFunction function) {
        switch (function) {
            case SIGMOID:
                return "sigmoid";
            case TANH:
                return "tanh";
            default:
                throw new IllegalArgumentException("Unknown Function: " + function.getClass().getName());
        }
    }

    public double func(double x) {
        return f.apply(x);
    }

    public double dFunc(double x) {
        return df.apply(x);
    }
}
