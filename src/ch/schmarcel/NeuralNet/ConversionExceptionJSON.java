package ch.schmarcel.NeuralNet;

public class ConversionExceptionJSON extends ConversionException {
    ConversionExceptionJSON(String message) {
        super(message);
    }

    ConversionExceptionJSON(String message, Throwable cause) {
        super(message, cause);
    }

    ConversionExceptionJSON(Throwable e) {
        super(e);
    }
}