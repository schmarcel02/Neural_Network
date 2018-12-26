package ch.schmarcel.NeuralNet;

public abstract class ConversionException extends Throwable{
    ConversionException(String message) {
        super(message);
    }

    ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    ConversionException(Throwable e) {
        super(e);
    }
}
