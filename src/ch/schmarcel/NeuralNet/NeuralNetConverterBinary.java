package ch.schmarcel.NeuralNet;

import ch.schmarcel.binary.BinaryStringIn;
import ch.schmarcel.binary.BinaryStringOut;
import ch.schmarcel.matrix.DoubleMatrix;

public class NeuralNetConverterBinary implements NeuralNetConverter {
    private BinaryStringIn bsi;
    private BinaryStringOut bso;

    @Override
    public byte[] neuralNetToByteArray(NeuralNet neuralNet) throws ConversionExceptionBinary {
        bso = new BinaryStringOut();

        String activationFunction = ActivationFunction.asString(neuralNet.activationFunction);
        bso.append(activationFunction);

        bso.append(neuralNet.layer_sizes.length);
        for (int i = 0; i < neuralNet.layer_sizes.length; i++) {
            bso.append(neuralNet.layer_sizes[i]);
        }

        bso.append(neuralNet.biases.length);
        for (int i = 0; i < neuralNet.biases.length; i++) {
            appendMatrix(neuralNet.biases[i]);
        }

        bso.append(neuralNet.weights.length);
        for (int i = 0; i < neuralNet.weights.length; i++) {
            appendMatrix(neuralNet.weights[i]);
        }

        return bso.getAllBytes();
    }

    @Override
    public NeuralNet neuralNetFromByteArray(byte[] bytes) throws ConversionExceptionBinary {
        bsi = new BinaryStringIn(bytes);

        String activationFunction = bsi.getString();

        int b = bsi.getInt();
        int[] layer_sizes = new int[b];
        for (int i = 0; i < b; i++) {
            layer_sizes[i] = bsi.getInt();
        }

        int c = bsi.getInt();
        DoubleMatrix[] biases = new DoubleMatrix[c];
        for (int i = 0; i < c; i++) {
            biases[i] = getMatrix();
        }

        int d = bsi.getInt();
        DoubleMatrix[] weights = new DoubleMatrix[d];
        for (int i = 0; i < d; i++) {
            weights[i] = getMatrix();
        }
        return new NeuralNet(weights, biases, ActivationFunction.forString(activationFunction), layer_sizes);
    }

    private void appendMatrix(DoubleMatrix matrix) throws ConversionExceptionBinary {
        bso.append(matrix.getWidth());
        bso.append(matrix.getHeight());

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                bso.append(matrix.getData()[x][y]);
            }
        }
    }

    private DoubleMatrix getMatrix() throws ConversionExceptionBinary {
        int width = bsi.getInt();
        int height = bsi.getInt();

        DoubleMatrix matrix = new DoubleMatrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matrix.getData()[x][y] = bsi.getDouble();
            }
        }
        return matrix;
    }
}
