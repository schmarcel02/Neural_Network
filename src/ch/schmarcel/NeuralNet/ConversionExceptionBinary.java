package ch.schmarcel.NeuralNet;

public class ConversionExceptionBinary extends ConversionException {
    ConversionExceptionBinary(String message) {
        super(message);
    }

    ConversionExceptionBinary(String message, Throwable cause) {
        super(message, cause);
    }

    ConversionExceptionBinary(Throwable e) {
        super(e);
    }
}
