package ch.schmarcel.NeuralNet;

public interface NeuralNetConverter {
    byte[] neuralNetToByteArray(NeuralNet neuralNet) throws ConversionException;

    NeuralNet neuralNetFromByteArray(byte[] bytes) throws ConversionException;
}
