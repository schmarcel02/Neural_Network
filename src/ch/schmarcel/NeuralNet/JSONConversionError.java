package ch.schmarcel.NeuralNet;

public class JSONConversionError extends Throwable {
    JSONConversionError(String message) {
        super(message);
    }

    JSONConversionError(String message, Throwable cause) {
        super(message, cause);
    }

    JSONConversionError(Throwable e) {
        super(e);
    }
}